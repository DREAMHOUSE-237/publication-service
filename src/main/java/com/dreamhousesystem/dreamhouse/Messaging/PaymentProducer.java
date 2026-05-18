package com.dreamhousesystem.dreamhouse.Messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentRequest(String proprietaireEmail, String description,
                                    Double prix, String numeroPaiement, Integer idPublication) { // ← idPublication ajouté
        PaymentRequestMessage message = new PaymentRequestMessage(
                proprietaireEmail, description, prix, numeroPaiement, idPublication // ← AJOUT
        );
        rabbitTemplate.convertAndSend("payment-exchange", "payment.routing.key", message);

        System.out.println("Message de paiement envoyé pour: " + proprietaireEmail);
        System.out.println("Numéro de paiement: " + numeroPaiement);
        System.out.println("Description: " + description);
        System.out.println("Prix: " + prix);
        System.out.println("Id publication: " + idPublication); // ← AJOUT
    }
}