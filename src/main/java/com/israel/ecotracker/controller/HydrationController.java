package com.israel.ecotracker.controller;

import com.israel.ecotracker.domain.HydrationLog;
import com.israel.ecotracker.dto.DailyHydrationResponse;
import com.israel.ecotracker.dto.HydrationLogRequest;
import com.israel.ecotracker.repository.HydrationLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/hydration")
public class HydrationController {

    private final HydrationLogRepository hydrationLogRepository;

    public HydrationController(HydrationLogRepository hydrationLogRepository) {
        this.hydrationLogRepository = hydrationLogRepository;
    }

    // 1. Save a new water log
    @PostMapping("/log")
    public ResponseEntity<Void> logWater(@RequestBody HydrationLogRequest request) {
        HydrationLog log = new HydrationLog(
                request.getUserId(),
                request.getAmount(),
                LocalDate.now()
        );

        hydrationLogRepository.save(log);
        return ResponseEntity.ok().build();
    }

    // 2. Get today's total for the UI
    @GetMapping("/{userId}/today")
    public ResponseEntity<DailyHydrationResponse> getTodayTotal(@PathVariable String userId) {
        int totalToday = hydrationLogRepository.getTotalHydrationForUserToday(userId, LocalDate.now());

        return ResponseEntity.ok(new DailyHydrationResponse(totalToday));
    }
}