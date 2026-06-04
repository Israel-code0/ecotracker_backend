package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hydration_logs")
public class HydrationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private int amount;
    private LocalDate logDate;

    public HydrationLog() {}

    public HydrationLog(String userId, int amount, LocalDate logDate) {
        this.userId = userId;
        this.amount = amount;
        this.logDate = logDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
}