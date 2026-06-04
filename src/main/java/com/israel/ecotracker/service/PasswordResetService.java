package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.OtpToken;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.repository.OtpTokenRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public boolean verifyAndResetPassword(String email, String otpCode, String newPassword) {
        OtpToken token = otpTokenRepository.findByEmailAndOtp(email, otpCode).orElse(null);

        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = userRepository.findByEmail(token.getEmail()).orElse(null);
        if (user == null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpTokenRepository.deleteByEmail(token.getEmail());

        return true;
    }
}