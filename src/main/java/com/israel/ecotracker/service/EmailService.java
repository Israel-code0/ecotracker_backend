package com.israel.ecotracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Value("${MAIL_PASSWORD:placeholder_key_for_local_dev}")
    private String apiKey; // We will map your Resend API Key right here

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtpEmail(String toEmail, String otp) {
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Resend API payload format
        Map<String, Object> body = Map.of(
                "from", "EcoTracker <onboarding@resend.dev>",
                "to", List.of(toEmail),
                "subject", "EcoTracker Password Reset Code",
                "text", "Your password reset code is: " + otp + "\n\nThis code will expire in 10 minutes."
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("HTTP Mail Service: OTP successfully dispatched to " + toEmail);
            } else {
                throw new RuntimeException("Resend API rejected request: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("!!! HTTP API MAIL DELIVERY FAILED !!!");
            throw new RuntimeException("Failed to dispatch email over HTTP: " + e.getMessage(), e);
        }
    }
}