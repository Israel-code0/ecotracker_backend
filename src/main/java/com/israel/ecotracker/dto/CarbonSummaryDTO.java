package com.israel.ecotracker.dto;

import com.israel.ecotracker.domain.ActivityLog;
import java.util.List;

public class CarbonSummaryDTO {
    private String userName;
    private Double annualCarbonGoal;
    private Double totalEmissionsToDate;
    private List<ActivityLog> activityHistory;

    public CarbonSummaryDTO(String userName, Double annualCarbonGoal, Double totalEmissionsToDate, List<ActivityLog> activityHistory) {
        this.userName = userName;
        this.annualCarbonGoal = annualCarbonGoal;
        this.totalEmissionsToDate = totalEmissionsToDate;
        this.activityHistory = activityHistory;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Double getAnnualCarbonGoal() { return annualCarbonGoal; }
    public void setAnnualCarbonGoal(Double annualCarbonGoal) { this.annualCarbonGoal = annualCarbonGoal; }
    public Double getTotalEmissionsToDate() { return totalEmissionsToDate; }
    public void setTotalEmissionsToDate(Double totalEmissionsToDate) { this.totalEmissionsToDate = totalEmissionsToDate; }
    public List<ActivityLog> getActivityHistory() { return activityHistory; }
    public void setActivityHistory(List<ActivityLog> activityHistory) { this.activityHistory = activityHistory; }
}