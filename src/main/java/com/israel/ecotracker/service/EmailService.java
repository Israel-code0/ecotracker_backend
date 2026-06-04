package com.israel.ecotracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("EcoTracker - Password Reset Code");
        message.setText("Hello,\n\n" +
                "Your 6-digit password reset code is: " + otp + "\n\n" +
                "This code will expire in 10 minutes. If you did not request this, please ignore this email.");

        // This simple command automatically reads your smtp.gmail.com settings from application.properties!
        mailSender.send(message);
    }
}