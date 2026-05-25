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

    // Constructor injection for repositories
    public CarbonLedgerService(ActivityLogRepository activityLogRepository,
                               UserRepository userRepository,
                               EmissionCategoryRepository emissionCategoryRepository) {
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
        this.emissionCategoryRepository = emissionCategoryRepository;
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

        // The carbon math (quantity * co2_per_unit) triggers automatically here via @PrePersist
        return activityLogRepository.save(log);
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