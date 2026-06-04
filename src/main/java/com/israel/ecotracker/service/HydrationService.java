package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.HydrationLog;
import com.israel.ecotracker.repository.HydrationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HydrationService {

    private final HydrationLogRepository hydrationLogRepository;
    private final BadgeService badgeService;

    @Autowired
    public HydrationService(HydrationLogRepository hydrationLogRepository, BadgeService badgeService) {
        this.hydrationLogRepository = hydrationLogRepository;
        this.badgeService = badgeService;
    }

    /**
     * Logs a water entry and triggers the gamification engine.
     */
    @Transactional
    public void logWater(String userId, int amountInMl) {
        HydrationLog log = new HydrationLog(userId, amountInMl, LocalDate.now());
        hydrationLogRepository.save(log);

        int dailyTotal = getDailyTotal(userId, LocalDate.now());

        if (dailyTotal > 0) {
            badgeService.evaluateAndAwardBadge(userId, "FIRST_SIP");
        }

        if (dailyTotal >= 2500) {
            badgeService.evaluateAndAwardBadge(userId, "HYDRATION_HERO");
        }
    }

    /**
     * Calculates the total amount of water drank by a user on a specific day.
     */
    @Transactional(readOnly = true)
    public int getDailyTotal(String userId, LocalDate date) {
        Integer total = hydrationLogRepository.sumWaterByUserIdAndDate(userId, date);
        return total != null ? total : 0;
    }

    /**
     * Optional: Reset the daily water (if your Flutter app has a reset button)
     */
    @Transactional
    public void resetDailyWater(String userId, LocalDate date) {
        hydrationLogRepository.deleteByUserIdAndDate(userId, date);
    }
}
