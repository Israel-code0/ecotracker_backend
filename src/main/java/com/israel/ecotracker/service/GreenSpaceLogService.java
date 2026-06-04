package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.GreenSpaceLog;
import com.israel.ecotracker.repository.GreenSpaceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreenSpaceLogService {

    private final GreenSpaceLogRepository greenSpaceLogRepository;
    private final BadgeService badgeService;

    @Autowired
    public GreenSpaceLogService(GreenSpaceLogRepository greenSpaceLogRepository, BadgeService badgeService) {
        this.greenSpaceLogRepository = greenSpaceLogRepository;
        this.badgeService = badgeService;
    }

    public void logSession(String userId, int minutes) {
        // 1. Save the new session to the vault
        GreenSpaceLog session = new GreenSpaceLog(userId, minutes, java.time.LocalDate.now());
        greenSpaceLogRepository.save(session);

        // 2. Fetch the newly updated lifetime total from the database
        int totalMinutes = getTotalMinutes(userId);

        // 3. 🚀 THE GAMIFICATION TRIGGERS
        if (totalMinutes > 0) {
            badgeService.evaluateAndAwardBadge(userId, "FIRST_STEP");
        }

        if (totalMinutes >= 60) {
            badgeService.evaluateAndAwardBadge(userId, "MINDFUL_MASTER");
        }
    }

    public int getTotalMinutes(String userId) {
        return greenSpaceLogRepository.sumTotalMinutesByUserId(userId);
    }
}