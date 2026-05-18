package com.dreamhousesystem.dreamhouse.Messaging;

import com.dreamhousesystem.dreamhouse.Entities.BienImmobilier;
import com.dreamhousesystem.dreamhouse.Entities.StatutPublication;
import com.dreamhousesystem.dreamhouse.Repositories.BienImmobilierRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusConsumer {

    private final BienImmobilierRepository repository;

    public PaymentStatusConsumer(BienImmobilierRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "payment-status")
    public void receivePaymentStatus(PaymentStatusMessage message) {
        System.out.println("Statut de paiement reçu : " + message.getStatus());
        System.out.println("Publication ID : " + message.getPublicationId());
        System.out.println("Correlation ID : " + message.getCorrelationId());

        Integer publicationId = message.getPublicationId();
        String status = message.getStatus();

        if (publicationId == null) {
            System.out.println("[PaymentStatusConsumer] publicationId absent, impossible de mettre à jour le bien.");
            return;
        }

        BienImmobilier bien = repository.findById(publicationId).orElse(null);
        if (bien == null) {
            System.out.println("[PaymentStatusConsumer] Bien introuvable pour id=" + publicationId);
            return;
        }

        // ✅ Activer ou rejeter la publication selon le statut de paiement
        if ("SUCCESS".equalsIgnoreCase(status) || "SUCCESSFUL".equalsIgnoreCase(status)) {
            bien.setStatutPublication(StatutPublication.ACTIVE);
            System.out.println("[PaymentStatusConsumer] Publication " + publicationId + " → ACTIVE");
        } else if ("FAILED".equalsIgnoreCase(status) || "FAIL".equalsIgnoreCase(status)) {
            bien.setStatutPublication(StatutPublication.REJETEE);
            System.out.println("[PaymentStatusConsumer] Publication " + publicationId + " → REJETEE");
        } else {
            // PENDING : on ne change rien, on attend le résultat final
            System.out.println("[PaymentStatusConsumer] Statut PENDING reçu pour " + publicationId + ", aucun changement.");
            return;
        }

        repository.save(bien);
    }

    // Gardé pour compatibilité avec l'ancien code commenté dans BienImmobilierServiceImpl
    public String getCurrentPaymentStatus() {
        return null; // plus utilisé, la logique est maintenant asynchrone via RabbitMQ
    }
}