package com.israel.ecotracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    // A public endpoint that requires no database lookups or authentication
    @GetMapping("/health")
    public ResponseEntity<String> keepAlive() {
        return ResponseEntity.ok("EcoTracker Server is Awake! 🌱");
    }
}
