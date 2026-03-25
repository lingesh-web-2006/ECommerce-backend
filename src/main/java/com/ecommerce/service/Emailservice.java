package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class Emailservice {

    @Autowired(required = false)
    private JavaMailSender mailsender;

    @Value("${mail.receive.protocol:imap}")
    private String receiveProtocol;

    @Value("${mail.receive.host:}")
    private String receiveHost;

    @Value("${mail.receive.port:993}")
    private int receivePort;

    @Value("${mail.receive.username:}")
    private String receiveUsername;

    @Value("${mail.receive.password:}")
    private String receivePassword;

    public void sendEmail(String toEmail, String subject, String message) {
        if (mailsender == null) {
            // Mail sender not configured; skip sending to avoid startup failure
            System.out.println("JavaMailSender not configured - skipping email send to: " + toEmail);
            return;
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            mailsender.send(mailMessage);
            System.out.println("Email sent to: " + toEmail);
        } catch (Exception ex) {
            System.err.println("Failed to send email to: " + toEmail + " - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Fetch recent message subjects from the configured IMAP/POP3 store.
     * Returns an empty list if receive settings are not configured or on error.
     */
    public List<String> fetchInboxSubjects(int max) {
        if (receiveHost == null || receiveHost.isBlank() || receiveUsername == null || receiveUsername.isBlank()) {
            return Collections.emptyList();
        }

        List<String> subjects = new ArrayList<>();
        Properties props = new Properties();
        props.put("mail.store.protocol", receiveProtocol);
        props.put(String.format("mail.%s.host", receiveProtocol), receiveHost);
        props.put(String.format("mail.%s.port", receiveProtocol), String.valueOf(receivePort));
        props.put(String.format("mail.%s.ssl.enable", receiveProtocol), "true");

        Session session = Session.getInstance(props);
        try (Store store = session.getStore(receiveProtocol)) {
            store.connect(receiveHost, receiveUsername, receivePassword);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();
            int start = Math.max(0, messages.length - max);
            for (int i = messages.length - 1; i >= start; i--) {
                try {
                    subjects.add(messages[i].getSubject());
                } catch (Exception e) {
                    subjects.add("[error reading subject]");
                }
            }
            inbox.close(false);
        } catch (Exception e) {
            System.out.println("Error fetching emails: " + e.getMessage());
            return Collections.emptyList();
        }

        return subjects;
    }
}