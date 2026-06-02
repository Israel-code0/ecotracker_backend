package com.israel.ecotracker.controller;

import com.israel.ecotracker.dto.EarnedBadgeResponse;
import com.israel.ecotracker.domain.EarnedBadge;
import com.israel.ecotracker.repository.EarnedBadgeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "*")
public class BadgeController {

    private final EarnedBadgeRepository badgeRepository;

    public BadgeController(EarnedBadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    // 1. Fetch all unlocked badges for the UI
    @GetMapping("/{userId}")
    public ResponseEntity<List<EarnedBadgeResponse>> getUserBadges(@PathVariable String userId) {
        List<EarnedBadgeResponse> badges = badgeRepository.findByUserId(userId)
                .stream()
                .map(badge -> new EarnedBadgeResponse(badge.getBadgeCode()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(badges);
    }

    // 2. Secret endpoint to award a badge (can be called by other Java services or for testing)
    @PostMapping("/award/{userId}")
    public ResponseEntity<String> awardBadge(@PathVariable String userId, @RequestBody Map<String, String> payload) {
        String badgeCode = payload.get("badgeCode");

        // Prevent duplicate badges
        if (badgeRepository.existsByUserIdAndBadgeCode(userId, badgeCode)) {
            return ResponseEntity.badRequest().body("Badge already unlocked!");
        }

        EarnedBadge newBadge = new EarnedBadge(userId, badgeCode, LocalDateTime.now());
        badgeRepository.save(newBadge);

        return ResponseEntity.ok("Badge " + badgeCode + " unlocked successfully!");
    }
}