package com.israel.ecotracker.domain;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class OtpToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String otp;
    private LocalDateTime expiryDate;

    // Default constructor
    public OtpToken() {}

    public OtpToken(String email, String otp, LocalDateTime expiryDate) {
        this.email = email;
        this.otp = otp;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
}