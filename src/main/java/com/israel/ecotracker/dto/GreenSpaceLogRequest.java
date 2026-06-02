package com.israel.ecotracker.dto;

public class GreenSpaceLogRequest {
    private String userId;
    private int minutes;

    public GreenSpaceLogRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}