package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.DTO.UtilisateurConnecte;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import com.dreamhousesystem.dreamhouse.Mappers.BienImmobilierMapper;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentProducer;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentStatusConsumer;
import com.dreamhousesystem.dreamhouse.Messaging.UserEmailConsumer;
import com.dreamhousesystem.dreamhouse.Repositories.BienImmobilierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BienImmobilierServiceImplTest {

    private static final UtilisateurConnecte ALICE = new UtilisateurConnecte("alice@example.cm", "proprietaire");
    private static final UtilisateurConnecte BOB = new UtilisateurConnecte("bob@example.cm", "proprietaire");
    private static final UtilisateurConnecte ADMIN = new UtilisateurConnecte("admin@example.cm", "admin");
    private static final UtilisateurConnecte ANONYME = new UtilisateurConnecte(null, null);

    private BienImmobilierRepository repository;
    private UserEmailConsumer userEmailConsumer;
    private PaymentProducer paymentProducer;
    private BienImmobilierServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(BienImmobilierRepository.class);
        userEmailConsumer = mock(UserEmailConsumer.class);
        paymentProducer = mock(PaymentProducer.class);
        service = new BienImmobilierServiceImpl(repository, new BienImmobilierMapper(),
                userEmailConsumer, paymentProducer, mock(PaymentStatusConsumer.class));
        when(repository.save(any(BienImmobilier.class))).thenAnswer(i -> i.getArgument(0));
    }

    private BienImmobilier bienDe(String email) {
        BienImmobilier bien = new BienImmobilier();
        bien.setId(7);
        bien.setProprietaireEmail(email);
        bien.setPrix(100000.0);
        bien.setTypePublication(TypePublication.LOCATION);
        when(repository.findById(7)).thenReturn(Optional.of(bien));
        return bien;
    }

    private static HttpStatus statut(ResponseStatusException e) {
        return HttpStatus.valueOf(e.getStatusCode().value());
    }

    @Test
    void createBienUsesTheRequestAuthorNotTheLastLoggedInUser() {
        // Ancien comportement : le dernier email reçu sur RabbitMQ (ici Bob) devenait propriétaire
        when(userEmailConsumer.getCurrentUserEmail()).thenReturn("bob@example.cm");
        BienImmobilier bien = new BienImmobilier();
        bien.setPrix(100000.0);
        bien.setTypePublication(TypePublication.LOCATION);
        bien.setProprietaireEmail("forge@example.cm");

        service.createBien(bien, ALICE);

        assertEquals("alice@example.cm", bien.getProprietaireEmail());
        verify(paymentProducer).sendPaymentRequest(
                org.mockito.ArgumentMatchers.eq("alice@example.cm"), any(), anyDouble(), any(), any());
    }

    @Test
    void createBienRequiresALoggedInUser() {
        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> service.createBien(new BienImmobilier(), ANONYME));
        assertEquals(HttpStatus.UNAUTHORIZED, statut(e));
        verify(repository, never()).save(any());
    }

    @Test
    void anotherUserCannotUpdateDeleteOrRetryPayment() {
        BienImmobilier bien = bienDe("alice@example.cm");

        assertEquals(HttpStatus.FORBIDDEN, statut(assertThrows(ResponseStatusException.class,
                () -> service.updateBien(7, new BienImmobilier(), BOB))));
        assertEquals(HttpStatus.FORBIDDEN, statut(assertThrows(ResponseStatusException.class,
                () -> service.deleteBien(7, BOB))));
        assertEquals(HttpStatus.FORBIDDEN, statut(assertThrows(ResponseStatusException.class,
                () -> service.retryPayment(7, "699000000", BOB))));

        verify(repository, never()).delete(any());
        verify(repository, never()).save(any());
        verify(paymentProducer, never()).sendPaymentRequest(anyString(), any(), anyDouble(), any(), anyInt());
        assertEquals("alice@example.cm", bien.getProprietaireEmail());
    }

    @Test
    void ownerCanUpdateAndKeepsOwnership() {
        BienImmobilier bien = bienDe("Alice@Example.cm");
        BienImmobilier modif = new BienImmobilier();
        modif.setPrix(120000.0);
        modif.setTypePublication(TypePublication.LOCATION);

        service.updateBien(7, modif, ALICE);

        verify(repository).save(bien);
        assertEquals(120000.0, bien.getPrix());
        assertEquals("Alice@Example.cm", bien.getProprietaireEmail());
    }

    @Test
    void adminCanDeleteAnyListing() {
        BienImmobilier bien = bienDe("alice@example.cm");
        service.deleteBien(7, ADMIN);
        verify(repository).delete(bien);
    }

    @Test
    void anonymousCannotDelete() {
        bienDe("alice@example.cm");
        assertEquals(HttpStatus.UNAUTHORIZED, statut(assertThrows(ResponseStatusException.class,
                () -> service.deleteBien(7, ANONYME))));
    }

    @Test
    void mesBiensListsTheRequesterListings() {
        when(repository.findByProprietaireEmailIgnoreCase("alice@example.cm")).thenReturn(List.of());
        service.findMesBiens(ALICE);
        verify(repository).findByProprietaireEmailIgnoreCase("alice@example.cm");
        verify(userEmailConsumer, never()).getCurrentUserEmail();
    }
}
