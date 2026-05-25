package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private EmissionCategory category;

    @Column(nullable = false)
    private Double quantity;

    @Column(name = "calculated_co2", nullable = false)
    private Double calculatedCo2;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    public ActivityLog() {}

    @PrePersist
    public void calculateCarbonImpact() {
        if (this.loggedAt == null) {
            this.loggedAt = LocalDateTime.now();
        }
        if (this.category != null && this.quantity != null) {
            this.calculatedCo2 = this.quantity * this.category.getCo2PerUnit();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public EmissionCategory getCategory() { return category; }
    public void setCategory(EmissionCategory category) { this.category = category; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public Double getCalculatedCo2() { return calculatedCo2; }
    public void setCalculatedCo2(Double calculatedCo2) { this.calculatedCo2 = calculatedCo2; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}