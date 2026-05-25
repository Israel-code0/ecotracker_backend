package com.israel.ecotracker.dto.auth;

public class SignupRequest {
    private String email;
    private String name;
    private String password;
    private Double annualCarbonGoal;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Double getAnnualCarbonGoal() { return annualCarbonGoal; }
    public void setAnnualCarbonGoal(Double annualCarbonGoal) { this.annualCarbonGoal = annualCarbonGoal; }
}