package com.israel.ecotracker.dto;

public class EarnedBadgeResponse {
    private String badgeCode;

    public EarnedBadgeResponse() {}

    public EarnedBadgeResponse(String badgeCode) {
        this.badgeCode = badgeCode;
    }

    public String getBadgeCode() {
        return badgeCode;
    }

    public void setBadgeCode(String badgeCode) {
        this.badgeCode = badgeCode;
    }
}