package com.israel.ecotracker.controller;

import com.israel.ecotracker.dto.GreenSpaceLogRequest;
import com.israel.ecotracker.dto.GreenSpaceSummaryResponse;
import com.israel.ecotracker.service.GreenSpaceLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/greenspace")
public class GreenSpaceLogController {


    private final GreenSpaceLogService greenSpaceLogService;

    public GreenSpaceLogController(GreenSpaceLogService greenSpaceLogService) {
        this.greenSpaceLogService = greenSpaceLogService;
    }

    @PostMapping("/log")
    public ResponseEntity<Void> logSession(@RequestBody GreenSpaceLogRequest request) {
        greenSpaceLogService.logSession(request.getUserId(), request.getMinutes());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{userId}/total")
    public ResponseEntity<GreenSpaceSummaryResponse> getLifetimeTotal(@PathVariable String userId) {
        int lifetimeTotal = greenSpaceLogService.getTotalMinutes(userId);
        return ResponseEntity.ok(new GreenSpaceSummaryResponse(lifetimeTotal));
    }
}