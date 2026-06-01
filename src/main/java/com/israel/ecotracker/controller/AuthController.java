package com.israel.ecotracker.controller;

import com.israel.ecotracker.domain.OtpToken;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.dto.auth.AuthResponse;
import com.israel.ecotracker.dto.auth.SignupRequest;
import com.israel.ecotracker.repository.OtpTokenRepository;
import com.israel.ecotracker.repository.UserRepository;
import com.israel.ecotracker.service.EmailService;
import com.israel.ecotracker.service.JwtService;
import com.israel.ecotracker.service.PasswordResetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already registered!");
        }

        // Encrypt password before saving to MySQL
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User(
                request.getEmail(),
                request.getName(),
                request.getAnnualCarbonGoal() != null ? request.getAnnualCarbonGoal() : 4000.0,
                hashedPassword
        );

        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser.getId(), savedUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, savedUser.getId(), savedUser.getName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email address or password.");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getName()));
    }

    @PostMapping("/forgot-password")
    @Transactional
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        otpTokenRepository.deleteByEmail(email);

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpToken token = new OtpToken(email, otp, LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(token);

        try {
            emailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok(java.util.Map.of("message", "Reset email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean isSuccess = passwordResetService.verifyAndResetPassword(token, newPassword);

        if (isSuccess) {
            return ResponseEntity.ok("Password successfully updated. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired reset code.");
        }
    }
}