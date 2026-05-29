package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.PasswordResetToken;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.repository.TokenRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Delete any old tokens this user might have requested before
            tokenRepository.deleteByUser(user);

            PasswordResetToken token = new PasswordResetToken(otp, user);
            tokenRepository.save(token);

            sendEmail(user.getEmail(), otp);
        }
    }

    private void sendEmail(String recipient, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("EcoTracker - Password Reset Code");
        message.setText("Hello,\n\nYour 6-digit password reset code is: " + otp +
                "\n\nThis code will expire in 15 minutes.");

        mailSender.send(message);
    }

    public boolean verifyAndResetPassword(String tokenString, String newPassword) {
        // 1. Look for the 6-digit code in the database
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(tokenString);

        if (tokenOptional.isEmpty()) {
            return false; // Code doesn't exist
        }

        PasswordResetToken token = tokenOptional.get();

        // 2. Check if the 15-minute timer ran out
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token); // Clean up the expired code
            return false;
        }

        // 3. Find the exact user this code belongs to
        User user = token.getUser();

        // 4. Encrypt the new password and save it
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 5. Destroy the token so it can never be used again!
        tokenRepository.delete(token);

        return true;
    }
}