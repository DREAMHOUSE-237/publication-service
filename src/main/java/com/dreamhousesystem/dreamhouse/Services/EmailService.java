/**
 * Envoi d'un email de confirmation simple (texte brut).
 *
 * @param to        Adresse email du destinataire
 * @param bienTitre Titre du bien publié
 */
package com.dreamhousesystem.dreamhouse.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.dreamhousesystem.dreamhouse.Messaging.UserEmailConsumer;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserEmailConsumer userEmailConsumer;

    @Autowired
    public EmailService(JavaMailSender mailSender,UserEmailConsumer userEmailConsumer) {
        this.mailSender = mailSender;
        this.userEmailConsumer = userEmailConsumer;
    }


    public void sendConfirmationEmail(String to, String bienTitre) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmation de publication sur DreamHouse");
        message.setText("Votre bien \"" + bienTitre + "\" a été publié avec succès sur DreamHouse !");
        message.setFrom("dreamhouse2372025@gmail.com");

        mailSender.send(message);
    }
}
