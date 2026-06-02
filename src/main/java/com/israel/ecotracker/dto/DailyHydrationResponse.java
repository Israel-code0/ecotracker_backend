package com.israel.ecotracker.dto; // (Make sure this matches your package)

public class DailyHydrationResponse {
    private int totalAmount;

    // Default constructor
    public DailyHydrationResponse() {}

    // Constructor with 1 argument (Fixes your "Expected no arguments" error!)
    public DailyHydrationResponse(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}