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
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
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
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already registered!"));
        }

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

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email address or password."));
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
            System.out.println("SMTP Diagnostics: Attempting connection to Google Mail server for: " + email);
            emailService.sendOtpEmail(email, otp);
            System.out.println("SMTP Diagnostics: Mail dispatched successfully!");

            // Returns clean JSON so Flutter's Dio doesn't crash on response parsing
            return ResponseEntity.ok(Map.of("message", "Reset email sent successfully"));
        } catch (Exception e) {
            System.out.println("!!! CRITICAL MAIL SYSTEM EXCEPTION !!!");
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Mail server failed: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");
        String newPassword = body.get("newPassword");

        if (email == null || otp == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email, OTP, and new password are required."));
        }

        boolean isSuccess = passwordResetService.verifyAndResetPassword(email, otp, newPassword);

        if (isSuccess) {
            return ResponseEntity.ok(Map.of("message", "Password successfully updated. You can now log in."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or expired reset code."));
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String newName = body.get("newName");

        if (userId == null || newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user data provided."));
        }

        try {
            // FIX 1: Parse the string into a UUID instead of a Long
            java.util.UUID userUuid = java.util.UUID.fromString(userId);
            Optional<User> userOptional = userRepository.findById(userUuid);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // FIX 2: Change this to match your User entity (e.g., setName, setUsername, setFullName)
                user.setName(newName);

                userRepository.save(user);

                return ResponseEntity.ok(Map.of("message", "Profile updated successfully."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "User not found."));
            }
        } catch (IllegalArgumentException e) {
            // Catches cases where the Flutter app sends a badly formatted UUID string
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user ID format."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update profile."));
        }
    }
}