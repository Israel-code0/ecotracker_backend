package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String badgeCode; // e.g., "POWER_SAVER", "TRANSIT_HERO", "FRESH_START"

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime unlockedAt;

    public Badge() {}

    public Badge(UUID userId, String badgeCode, String title) {
        this.userId = userId;
        this.badgeCode = badgeCode;
        this.title = title;
        this.unlockedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getBadgeCode() { return badgeCode; }
    public String getTitle() { return title; }
    public LocalDateTime getUnlockedAt() { return unlockedAt; }
}