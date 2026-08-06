package com.rusmessanger.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class EmailService {
    // Конфигурация SMTP: замените на реальные параметры
    private final String smtpHost = System.getenv().getOrDefault("SMTP_HOST", "smtp.example.com");
    private final String smtpPort = System.getenv().getOrDefault("SMTP_PORT", "587");
    private final String smtpUser = System.getenv().getOrDefault("SMTP_USER", "user@example.com");
    private final String smtpPass = System.getenv().getOrDefault("SMTP_PASS", "password");

    public void sendVerificationCode(String toEmail, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPass);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(smtpUser, "RUS MESSANGER"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        msg.setSubject("Код подтверждения RUS MESSANGER");
        msg.setText("Ваш код подтверждения: " + code);
        msg.setHeader("X-Mailer", "RUS MESSANGER");
        Transport.send(msg);
    }

    public String generateCode() {
        int c = new Random().nextInt(900000) + 100000;
        return String.valueOf(c);
    }
}
