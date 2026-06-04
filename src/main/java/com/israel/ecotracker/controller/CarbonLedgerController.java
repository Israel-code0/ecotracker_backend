package com.israel.ecotracker.controller;

import com.israel.ecotracker.domain.ActivityLog;
import com.israel.ecotracker.domain.EarnedBadge;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.dto.CarbonSummaryDTO;
import com.israel.ecotracker.repository.UserRepository;
import com.israel.ecotracker.service.AutomationEngineService;
import com.israel.ecotracker.service.BadgeService;
import com.israel.ecotracker.service.CarbonLedgerService;
import com.israel.ecotracker.service.InsightsEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/carbon")
@CrossOrigin("*")
public class CarbonLedgerController {

    private final CarbonLedgerService carbonLedgerService;
    private final UserRepository userRepository;
    private final InsightsEngineService insightsEngineService;
    private final AutomationEngineService automationEngineService;
    private final BadgeService badgeService;

    public CarbonLedgerController(CarbonLedgerService carbonLedgerService,
                                  UserRepository userRepository,
                                  InsightsEngineService insightsEngineService,
                                  AutomationEngineService automationEngineService,
                                  BadgeService badgeService) {
        this.carbonLedgerService = carbonLedgerService;
        this.userRepository = userRepository;
        this.insightsEngineService = insightsEngineService;
        this.automationEngineService = automationEngineService;
        this.badgeService = badgeService;
    }

    /**
     * POST endpoint to record a new footprint entry.
     */
    @PostMapping("/logs")
    public ResponseEntity<ActivityLog> logNewActivity(@RequestBody Map<String, Object> payload) {
        try {
            UUID userId = UUID.fromString(payload.get("userId").toString());
            Long categoryId = Long.valueOf(payload.get("categoryId").toString());
            Double quantity = Double.valueOf(payload.get("quantity").toString());

            ActivityLog savedLog = carbonLedgerService.logActivity(userId, categoryId, quantity);
            return ResponseEntity.ok(savedLog);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET endpoint that compiles a unified dashboard breakdown for the frontend.
     */
    @GetMapping("/summary/{userId}")
    public ResponseEntity<CarbonSummaryDTO> getDashboardSummary(@PathVariable UUID userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Double totalEmissions = carbonLedgerService.getTotalEmissions(userId);
            List<ActivityLog> history = carbonLedgerService.getUserLedger(userId);

            CarbonSummaryDTO summary = new CarbonSummaryDTO(
                    user.getName(),
                    user.getAnnualCarbonGoal(),
                    totalEmissions,
                    history
            );

            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET endpoint fetching dynamically processed actionable insights for a user.
     */
    @GetMapping("/insights/{userId}")
    public ResponseEntity<List<com.israel.ecotracker.dto.InsightCardDTO>> getUserInsights(@PathVariable UUID userId) {
        try {
            List<com.israel.ecotracker.dto.InsightCardDTO> cards = insightsEngineService.generateInsights(userId);
            return ResponseEntity.ok(cards);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET endpoint for fetching the user's unlocked achievements.
     */
    @GetMapping("/badges/{userId}")
    public ResponseEntity<List<EarnedBadge>> getUserBadges(@PathVariable UUID userId) {
        automationEngineService.evaluateAndAwardBadges(userId);

        List<EarnedBadge> unlockedBadges = badgeService.getUserBadges(userId.toString());

        return ResponseEntity.ok(unlockedBadges);
    }
}