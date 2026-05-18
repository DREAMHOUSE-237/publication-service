package com.dreamhousesystem.dreamhouse.Messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEmailConsumer {

    private String currentUserEmail;
    private String currentRegion;
    private String currentRegionDisplay;

    @RabbitListener(queues = "user-email-queue")
    public void receiveUserEmail(UserEmailMessage message) {
        this.currentUserEmail = message.getEmail();
        this.currentRegion=message.getRegion();
        this.currentRegionDisplay=message.getRegion_display();
        System.out.println("📩 Email reçu depuis RabbitMQ : " + currentUserEmail);
        System.out.println("🆔 UserId reçu : " + message.getUserId());
        System.out.println("la region courante est : "+currentRegion);
        System.out.println("La region display courante est :"+currentRegionDisplay);
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }
    public String getCurrentRegion(){return currentRegion;}
    public String getCurrentRegionDisplay(){return currentRegionDisplay;}
}
