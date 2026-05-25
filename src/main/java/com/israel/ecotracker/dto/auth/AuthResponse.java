package com.israel.ecotracker.dto.auth;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String name;

    public AuthResponse(String token, UUID userId, String name) {
        this.token = token;
        this.userId = userId;
        this.name = name;
    }

    // Getters
    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
}