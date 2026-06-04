package com.israel.ecotracker.domain;

import jakarta.persistence.*;
        import java.time.LocalDateTime;

@Entity
@Table(name = "earned_badges")
public class EarnedBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String badgeCode;
    private LocalDateTime earnedAt;

    public EarnedBadge() {}

    public EarnedBadge(String userId, String badgeCode, LocalDateTime earnedAt) {
        this.userId = userId;
        this.badgeCode = badgeCode;
        this.earnedAt = earnedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getBadgeCode() { return badgeCode; }
    public LocalDateTime getEarnedAt() { return earnedAt; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setBadgeCode(String badgeCode) { this.badgeCode = badgeCode; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }
}