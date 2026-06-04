package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.ActivityLog;
import com.israel.ecotracker.domain.EmissionCategory;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.repository.ActivityLogRepository;
import com.israel.ecotracker.repository.EmissionCategoryRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CarbonLedgerService {

    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final EmissionCategoryRepository emissionCategoryRepository;
    private final BadgeService badgeService;

    // Constructor injection for repositories AND the BadgeService
    public CarbonLedgerService(ActivityLogRepository activityLogRepository,
                               UserRepository userRepository,
                               EmissionCategoryRepository emissionCategoryRepository,
                               BadgeService badgeService) {
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
        this.emissionCategoryRepository = emissionCategoryRepository;
        this.badgeService = badgeService;
    }

    /**
     * Logs a new carbon activity for a user and calculates its CO2 footprint.
     */
    @Transactional
    public ActivityLog logActivity(UUID userId, Long categoryId, Double quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        EmissionCategory category = emissionCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Emission category not found with ID: " + categoryId));

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setCategory(category);
        log.setQuantity(quantity);

        ActivityLog savedLog = activityLogRepository.save(log);

        List<ActivityLog> allLogs = getUserLedger(userId);
        int totalActivitiesLogged = allLogs.size();

        String userIdString = userId.toString();

        if (totalActivitiesLogged == 1) {
            badgeService.evaluateAndAwardBadge(userIdString, "FIRST_IMPACT");
        }

        if (totalActivitiesLogged >= 10) {
            badgeService.evaluateAndAwardBadge(userIdString, "GRID_GUARDIAN");
        }

        return savedLog;
    }

    /**
     * Fetches the complete historical ledger of entries for a single user.
     */
    @Transactional(readOnly = true)
    public List<ActivityLog> getUserLedger(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        return activityLogRepository.findByUserIdOrderByLoggedAtDesc(userId);
    }

    /**
     * Tally up the cumulative kilograms of CO2 a user has emitted across all logs.
     */
    @Transactional(readOnly = true)
    public Double getTotalEmissions(UUID userId) {
        List<ActivityLog> logs = activityLogRepository.findByUserIdOrderByLoggedAtDesc(userId);

        return logs.stream()
                .mapToDouble(ActivityLog::getCalculatedCo2)
                .sum();
    }
}