package com.edcapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

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
    
    /**
     * Sends an HTML mail (supports inline styles, links, etc.)
     */
    public void sendHtmlMail(String to, String subject, String htmlBody) {
        try {
        	
        	System.out.println("Before HTML mail sent successfully to: " + to+" :subject: "+subject);
        	
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("plmadm@vecv.in");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML
            mailSender.send(mimeMessage);

            System.out.println("HTML mail sent successfully to: " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Error sending HTML mail: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

