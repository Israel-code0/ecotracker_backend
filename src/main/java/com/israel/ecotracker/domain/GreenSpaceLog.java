package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "green_space_logs")
public class GreenSpaceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private int minutes;
    private LocalDate logDate;

    public GreenSpaceLog() {}

    public GreenSpaceLog(String userId, int minutes, LocalDate logDate) {
        this.userId = userId;
        this.minutes = minutes;
        this.logDate = logDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { this.minutes = minutes;}

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
}