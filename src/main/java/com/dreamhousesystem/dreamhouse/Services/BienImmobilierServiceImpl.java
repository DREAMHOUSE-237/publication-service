package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.DTO.BienImmobilierDTO;
import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.CategorieBien;
import com.dreamhousesystem.dreamhouse.Entities.StatutPublication;
import com.dreamhousesystem.dreamhouse.Entities.TypePublication;
import com.dreamhousesystem.dreamhouse.Mappers.BienImmobilierMapper;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentProducer;
import com.dreamhousesystem.dreamhouse.Messaging.PaymentStatusConsumer;
import com.dreamhousesystem.dreamhouse.Messaging.UserEmailConsumer;
import com.dreamhousesystem.dreamhouse.Repositories.BienImmobilierRepository;
import com.dreamhousesystem.dreamhouse.strategie.ContextePrix;
import com.dreamhousesystem.dreamhouse.strategie.StrategiePrixLocation;
import com.dreamhousesystem.dreamhouse.strategie.StrategiePrixVente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BienImmobilierServiceImpl implements BienImmobilierService {

    private final BienImmobilierRepository repository;
    private final BienImmobilierMapper mapper;
    private final UserEmailConsumer userEmailConsumer;
    private final PaymentProducer paymentProducer;
    private final PaymentStatusConsumer paymentStatusConsumer;

    public BienImmobilierServiceImpl(BienImmobilierRepository repository,
                                     BienImmobilierMapper mapper,
                                     UserEmailConsumer userEmailConsumer,
                                     PaymentProducer paymentProducer,
                                     PaymentStatusConsumer paymentStatusConsumer) {
        this.repository = repository;
        this.mapper = mapper;
        this.userEmailConsumer = userEmailConsumer;
        this.paymentProducer = paymentProducer;
        this.paymentStatusConsumer = paymentStatusConsumer;
    }

    @Override
    @Transactional
    public BienImmobilierDTO createBien(BienImmobilier bien) {
        if (bien.getImages() == null) bien.setImages(new ArrayList<>());
        if (bien.getDocuements() == null) bien.setDocuements(new ArrayList<>());

        String email = userEmailConsumer.getCurrentUserEmail();
        bien.setProprietaireEmail(email);

        // ✅ Statut EN_ATTENTE forcé à la création (aussi géré dans @PrePersist)
        bien.setStatutPublication(StatutPublication.EN_ATTENTE);

        // Calcul du prix selon la stratégie
        ContextePrix contexte = new ContextePrix();
        switch (bien.getTypePublication()) {
            case VENTE -> contexte.definirStrategie(new StrategiePrixVente());
            case LOCATION -> contexte.definirStrategie(new StrategiePrixLocation());
            default -> throw new IllegalArgumentException("Type de publication non supporte");
        }
        double prixFinal = contexte.appliquerStrategie(bien.getPrix());
        System.out.println("Prix calcule avec strategie : " + prixFinal);

        // ✅ Sauvegarder D'ABORD pour avoir l'id généré, avec statut EN_ATTENTE
        BienImmobilier bienEnregistre = repository.save(bien);

        // ✅ Envoyer le message de paiement APRÈS la sauvegarde avec l'id réel
        paymentProducer.sendPaymentRequest(
                bienEnregistre.getProprietaireEmail(),
                bienEnregistre.getDescription(),
                prixFinal,
                bienEnregistre.getNumeroPaiement(),
                bienEnregistre.getId()
        );

        // Retourne le bien EN_ATTENTE → le frontend peut afficher "paiement en cours"
        return mapper.toDTO(bienEnregistre);
    }

    @Override
    @Transactional
    public BienImmobilierDTO updateBien(int id, BienImmobilier bien) {
        BienImmobilier existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien non trouvé"));

        existing.setSuperfie(bien.getSuperfie());
        existing.setNbrePiece(bien.getNbrePiece());
        existing.setPrix(bien.getPrix());
        existing.setDescription(bien.getDescription());
        existing.setCategorie(bien.getCategorie());
        existing.setTypePublication(bien.getTypePublication());
        existing.setAdresse(bien.getAdresse());
        existing.setImages(bien.getImages() != null ? bien.getImages() : new ArrayList<>());
        existing.setDocuements(bien.getDocuements() != null ? bien.getDocuements() : new ArrayList<>());
        existing.setProprietaireEmail(userEmailConsumer.getCurrentUserEmail());
        // ✅ Ne pas toucher au statutPublication lors d'un update

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void deleteBien(int id) {
        repository.deleteById(id);
    }

    @Override
    public BienImmobilierDTO getBienById(int id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Bien non trouvé"));
    }

    @Override
    public List<BienImmobilierDTO> getAllBiens() {
        // ✅ Uniquement les biens ACTIVES visibles publiquement
        return repository.findByStatutPublication(StatutPublication.ACTIVE)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByCategorie(CategorieBien categorie) {
        return repository.findByCategorieAndStatutPublication(categorie, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByTypePublication(TypePublication typePublication) {
        return repository.findByTypePublicationAndStatutPublication(typePublication, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByPrixMax(Double prix) {
        return repository.findByPrixLessThanEqualAndStatutPublication(prix, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByNbrePieceMin(int nbrePiece) {
        return repository.findByNbrePieceGreaterThanEqualAndStatutPublication(nbrePiece, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByVille(String ville) {
        return repository.findByAdresse_VilleIgnoreCaseAndStatutPublication(ville, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> finByQuartier(String quartier) {
        return repository.findByAdresse_QuartierAndStatutPublication(quartier, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> getRecentPublications() {
        return repository.findRecentPublications()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByRegion(String region) {
        return repository.findByAdresse_RegionAndStatutPublication(region, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByRegionCurrent() {
        String region = userEmailConsumer.getCurrentRegionDisplay();
        return findByRegion(region);
    }

    @Override
    public List<BienImmobilierDTO> findByVilleAndPrix(String ville, Double prix) {
        return repository.findByAdresse_VilleAndPrixLessThanEqualAndStatutPublication(
                        ville, prix, StatutPublication.ACTIVE)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findByProprietaireEmail(String email) {
        // ✅ Le propriétaire voit TOUS ses biens (EN_ATTENTE, ACTIVE, REJETEE)
        return repository.findByProprietaireEmailIgnoreCase(email)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BienImmobilierDTO> findMesBiens() {
        String userEmail = userEmailConsumer.getCurrentUserEmail();
        if (userEmail == null) {
            throw new RuntimeException("Veillez vous connecter pour visualiser vos publications");
        }
        // ✅ Le propriétaire voit TOUS ses biens (EN_ATTENTE, ACTIVE, REJETEE)
        return repository.findByProprietaireEmailIgnoreCase(userEmail)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}