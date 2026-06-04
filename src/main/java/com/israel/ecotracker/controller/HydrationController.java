package com.israel.ecotracker.controller;

import com.israel.ecotracker.dto.DailyHydrationResponse;
import com.israel.ecotracker.service.HydrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/hydration")
public class HydrationController {

    private final HydrationService hydrationService;

    public HydrationController(HydrationService hydrationService) {
        this.hydrationService = hydrationService;
    }

    @PostMapping("/log")
    public ResponseEntity<String> logWater(@RequestBody Map<String, Object> payload) {
        try {
            String userId = payload.get("userId").toString();
            int amount = Integer.parseInt(payload.get("amount").toString());

            hydrationService.logWater(userId, amount);
            return ResponseEntity.ok("Water logged successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid hydration data");
        }
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<DailyHydrationResponse> getTodayTotal(@PathVariable String userId) {

        int totalToday = hydrationService.getDailyTotal(userId, LocalDate.now());

        return ResponseEntity.ok(new DailyHydrationResponse(totalToday));
    }
}