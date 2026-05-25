package com.israel.ecotracker.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "annual_carbon_goal", nullable = false)
    private Double annualCarbonGoal;

    @Column(nullable = false)
    private String password;

    public User() {}

    public User(String email, String name, Double annualCarbonGoal, String password) {
        this.email = email;
        this.name = name;
        this.annualCarbonGoal = annualCarbonGoal;
        this.password = password;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getAnnualCarbonGoal() { return annualCarbonGoal; }
    public void setAnnualCarbonGoal(Double annualCarbonGoal) { this.annualCarbonGoal = annualCarbonGoal; }
    public String getPassword() {return password; }
    public void setPassword(String password) {this.password = password; }
}