package com.sudhir003.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String msg) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("sudhirat2004@gmail.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(msg);

        mailSender.send(mail);
    }
}
