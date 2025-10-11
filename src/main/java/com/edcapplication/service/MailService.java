package com.edcapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestBedEntryMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    
    /**
     * Send mail to multiple recipients with optional BCC
     * @param to array of main recipients
     * @param subject email subject
     * @param body email body
     * @param bcc array of BCC recipients
     */
    public void sendMail(String[] to, String subject, String body, String... bcc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        if (bcc != null && bcc.length > 0) {
            message.setBcc(bcc);
        }

        mailSender.send(message);
    }
}

