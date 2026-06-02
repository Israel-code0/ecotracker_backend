package com.israel.ecotracker.dto;

public class GreenSpaceSummaryResponse {
    private int totalMinutes;

    public GreenSpaceSummaryResponse() {}

    public GreenSpaceSummaryResponse(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }
}