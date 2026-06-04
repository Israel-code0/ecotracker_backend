package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.Badge;
import com.israel.ecotracker.domain.EarnedBadge;
import com.israel.ecotracker.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;

    @Autowired
    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    // 1. The Fetch Mechanism (Called by your BadgeController)
    public List<EarnedBadge> getUserBadges(String userId) {
        return badgeRepository.findByUserId(userId);
    }

    // 2. The Gamification Engine (Called by your other Services)
    public void evaluateAndAwardBadge(String userId, String badgeCode) {
        // We check the database first so we don't accidentally give them the same badge twice!
        if (!badgeRepository.existsByUserIdAndBadgeCode(userId, badgeCode)) {
            EarnedBadge newBadge = new EarnedBadge(userId, badgeCode, LocalDateTime.now()); // Use your actual Badge entity constructor
            badgeRepository.save(newBadge);
            System.out.println("🏆 SERVER UNLOCK: " + badgeCode + " awarded to " + userId);
        }
    }
}