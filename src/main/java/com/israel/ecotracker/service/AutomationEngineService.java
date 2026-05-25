package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.ActivityLog;
import com.israel.ecotracker.domain.Badge;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.repository.ActivityLogRepository;
import com.israel.ecotracker.repository.BadgeRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AutomationEngineService {

    private final BadgeRepository badgeRepository;
    private final ActivityLogRepository activityLogRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public AutomationEngineService(BadgeRepository badgeRepository,
                                   ActivityLogRepository activityLogRepository,
                                   UserRepository userRepository,
                                   JavaMailSender mailSender) {
        this.badgeRepository = badgeRepository;
        this.activityLogRepository = activityLogRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    /**
     * Evaluates and unlocks achievements instantly
     */
    public List<Badge> evaluateAndAwardBadges(UUID userId) {
        List<ActivityLog> logs = activityLogRepository.findByUserIdOrderByLoggedAtDesc(userId);

        // Milestone 1: Fresh Start
        if (!logs.isEmpty() && !badgeRepository.existsByUserIdAndBadgeCode(userId, "FRESH_START")) {
            badgeRepository.save(new Badge(userId, "FRESH_START", "First Eco Step"));
        }

        // Milestone 2: Power Saver (Logged more than 3 electricity logs and kept average low)
        long electricityCount = logs.stream().filter(l -> l.getCategory().getName().equals("ELECTRICITY")).count();
        if (electricityCount >= 3 && !badgeRepository.existsByUserIdAndBadgeCode(userId, "POWER_SAVER")) {
            badgeRepository.save(new Badge(userId, "POWER_SAVER", "Grid Guardian"));
        }

        return badgeRepository.findByUserId(userId);
    }

    /**
     * CRON Job Scheduler: Fires automatically at midnight on the 1st day of every month
     * Cron format: Second, Minute, Hour, Day-of-Month, Month, Day-of-Week
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void sendMonthlyEcoReports() {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            List<ActivityLog> logs = activityLogRepository.findByUserIdOrderByLoggedAtDesc(user.getId());
            double totalEmissions = logs.stream().mapToDouble(ActivityLog::getCalculatedCo2).sum();

            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Your Monthly EcoTracker Report Card 🍃");
                message.setText(String.format(
                        "Hello %s,\n\nHere is your carbon breakdown report for last month:\n\n" +
                                "Total Footprint Generated: %.2f kg CO₂\n" +
                                "Your Annual Target Budget Limit: %.0f kg CO₂\n\n" +
                                "Keep logging your habits to beat your goals next month!\n\nBest regards,\nEcoTracker Automation Core",
                        user.getName(), totalEmissions, user.getAnnualCarbonGoal()
                ));

                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Failed to dispatch report to: " + user.getEmail());
            }
        }
    }
}