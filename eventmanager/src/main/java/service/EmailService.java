package com.example.eventmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendRegistrationConfirmation(String to, String eventName, String eventDate, String location) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Confirmare înregistrare - " + eventName);
            message.setText(
                    "Bună ziua!\n\n" +
                            "Te-ai înregistrat cu succes la evenimentul: " + eventName + "\n" +
                            "Data: " + eventDate + "\n" +
                            "Locație: " + location + "\n\n" +
                            "Ne vedem acolo!\n\n" +
                            "Event Manager Team"
            );
            mailSender.send(message);
            log.info("Confirmation email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}