package com.israel.ecotracker.dto;

public class InsightCardDTO {
    private String title;
    private String description;
    private String impactLevel; // e.g., "HIGH", "MEDIUM", "INFO"
    private String actionButtonText;

    public InsightCardDTO(String title, String description, String impactLevel, String actionButtonText) {
        this.title = title;
        this.description = description;
        this.impactLevel = impactLevel;
        this.actionButtonText = actionButtonText;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImpactLevel() { return impactLevel; }
    public String getActionButtonText() { return actionButtonText; }
}