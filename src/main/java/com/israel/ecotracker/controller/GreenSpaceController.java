package com.israel.ecotracker.controller;

import com.israel.ecotracker.dto.GreenSpaceLogRequest;
import com.israel.ecotracker.dto.GreenSpaceSummaryResponse;
import com.israel.ecotracker.domain.GreenSpaceLog;
import com.israel.ecotracker.repository.GreenSpaceLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/greenspace")
public class GreenSpaceController {

    private final GreenSpaceLogRepository greenSpaceLogRepository;

    public GreenSpaceController(GreenSpaceLogRepository greenSpaceLogRepository) {
        this.greenSpaceLogRepository = greenSpaceLogRepository;
    }

    // 1. Save a new completed nature session
    @PostMapping("/log")
    public ResponseEntity<Void> logSession(@RequestBody GreenSpaceLogRequest request) {
        GreenSpaceLog log = new GreenSpaceLog(
                request.getUserId(),
                request.getMinutes(),
                LocalDate.now()
        );

        greenSpaceLogRepository.save(log);
        return ResponseEntity.ok().build();
    }

    // 2. Get lifetime total for the UI
    @GetMapping("/{userId}/total")
    public ResponseEntity<GreenSpaceSummaryResponse> getLifetimeTotal(@PathVariable String userId) {
        int lifetimeTotal = greenSpaceLogRepository.getLifetimeTotalMinutesForUser(userId);

        return ResponseEntity.ok(new GreenSpaceSummaryResponse(lifetimeTotal));
    }
}