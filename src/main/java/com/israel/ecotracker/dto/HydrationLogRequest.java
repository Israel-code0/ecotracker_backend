package com.israel.ecotracker.dto;

public class HydrationLogRequest {
    private String userId;
    private int amount;

    // Default constructor needed for JSON parsing
    public HydrationLogRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}