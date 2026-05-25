package com.israel.ecotracker.controller;

import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.dto.auth.AuthResponse;
import com.israel.ecotracker.dto.auth.SignupRequest;
import com.israel.ecotracker.repository.UserRepository;
import com.israel.ecotracker.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

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
}