package com.dreamhousesystem.dreamhouse.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusConsumer {

    private String currentPaymentStatus;

    @RabbitListener(queues = "payment-status")
    public void receivePaymentStatus(PaymentStatusMessage message) {
        this.currentPaymentStatus = message.getStatus();
        System.out.println(" Statut de paiement reçu depuis RabbitMQ : " + currentPaymentStatus);
        System.out.println(" PaymentId reçu : " + message.getPaymentId());
        System.out.println(" UserId reçu : " + message.getUserId());
    }

    public String getCurrentPaymentStatus() {
        return currentPaymentStatus;
    }
}
