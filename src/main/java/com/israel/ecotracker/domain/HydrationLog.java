package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hydration_logs")
public class HydrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    final private String userId;
    final private int amount;
    final private LocalDate logDate;


    public HydrationLog(String userId, int amount, LocalDate logDate) {
        this.userId = userId;
        this.amount = amount;
        this.logDate = logDate;
    }

    // Getters and Setters...
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public int getAmount() { return amount; }
    public LocalDate getLogDate() { return logDate; }
}
