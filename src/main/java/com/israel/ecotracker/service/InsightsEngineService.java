package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.ActivityLog;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.dto.InsightCardDTO;
import com.israel.ecotracker.repository.ActivityLogRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsightsEngineService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;

    public InsightsEngineService(ActivityLogRepository activityLogRepository, UserRepository userRepository) {
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
    }

    public List<InsightCardDTO> generateInsights(UUID userId) {
        List<InsightCardDTO> insights = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<ActivityLog> logs = activityLogRepository.findByUserIdOrderByLoggedAtDesc(userId);

        if (logs.isEmpty()) {
            insights.add(new InsightCardDTO(
                    "Fresh Start!",
                    "You haven't logged any activities yet. Log your first commute or utility bill to get custom insights.",
                    "INFO",
                    "Log Activity"
            ));
            return insights;
        }

        // 1. Calculate overall budget pressure
        double totalEmissions = logs.stream().mapToDouble(ActivityLog::getCalculatedCo2).sum();
        double budgetUsedPercentage = (totalEmissions / user.getAnnualCarbonGoal()) * 100;

        if (budgetUsedPercentage >= 85.0) {
            insights.add(new InsightCardDTO(
                    "Budget Warning",
                    String.format("You have exhausted %.1f%% of your annual carbon allowance. We recommend prioritizing low-carbon transport this week.", budgetUsedPercentage),
                    "HIGH",
                    "View Budget Plan"
            ));
        }

        // 2. Identify the highest emission category
        Map<String, Double> categoryTotals = logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getCategory().getName(),
                        Collectors.summingDouble(ActivityLog::getCalculatedCo2)
                ));

        String highestCategory = Collections.max(categoryTotals.entrySet(), Map.Entry.comparingByValue()).getKey();

        // 3. Generate category-specific actionable advice
        switch (highestCategory) {
            case "GASOLINE_VEHICLE":
                insights.add(new InsightCardDTO(
                        "Optimize Your Commute",
                        "Vehicle transit is your highest emission driver. Switching just two commutes this week to carpooling or public transit will cut your weekly total by up to 15 kg of CO₂.",
                        "MEDIUM",
                        "Explore Transit Paths"
                ));
                break;
            case "ELECTRICITY":
                insights.add(new InsightCardDTO(
                        "Phantom Energy Drain",
                        "Your home electricity usage is higher than your historical average. Unplugging devices on standby mode can reduce base grid consumption by 5% monthly.",
                        "MEDIUM",
                        "Read Eco-Home Guide"
                ));
                break;
            case "DIETARY_MEAT":
                insights.add(new InsightCardDTO(
                        "Green Plate Alternative",
                        "Dietary logs show heavy emissions from meat meals. Substituting just one meat dish with a plant-based alternative reduces your food footprint by 2.5 kg of CO₂ per meal.",
                        "INFO",
                        "See Green Recipes"
                ));
                break;
            default:
                insights.add(new InsightCardDTO(
                        "Keep It Up!",
                        "Your footprint is stable. Try challenging yourself to a carbon-free weekend to accelerate your progress.",
                        "INFO",
                        "Start a Challenge"
                ));
        }

        return insights;
    }
}