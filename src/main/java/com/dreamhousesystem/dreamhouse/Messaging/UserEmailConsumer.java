package com.dreamhousesystem.dreamhouse.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEmailConsumer {

    private String currentUserEmail;

    @RabbitListener(queues = "user-email-queue")
    public void receiveUserEmail(UserEmailMessage message) {
        this.currentUserEmail = message.getEmail();
        System.out.println("📩 Email reçu depuis RabbitMQ : " + currentUserEmail);
        System.out.println("🆔 UserId reçu : " + message.getUserId());
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
}
