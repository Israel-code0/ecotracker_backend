package com.israel.ecotracker.config;

import com.israel.ecotracker.domain.EmissionCategory;
import com.israel.ecotracker.domain.User;
import com.israel.ecotracker.repository.EmissionCategoryRepository;
import com.israel.ecotracker.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner seedDatabase(EmissionCategoryRepository categoryRepo, UserRepository userRepo) {
        return args -> {
            // 1. Seed Carbon Calculation Factors if empty
            if (categoryRepo.count() == 0) {
                categoryRepo.save(new EmissionCategory("GASOLINE_VEHICLE", "MILES", 0.404)); // 0.404 kg CO2 per mile
                categoryRepo.save(new EmissionCategory("ELECTRICITY", "KWH", 0.385));       // 0.385 kg CO2 per kWh
                categoryRepo.save(new EmissionCategory("FLIGHT_SHORT_HAUL", "HOURS", 150.0)); // 150 kg CO2 per hour
                categoryRepo.save(new EmissionCategory("DIETARY_MEAT", "MEALS", 2.5));       // 2.5 kg CO2 per heavy meat meal
                System.out.println("🌱 Database Seeder: Standard emission categories successfully seeded into MySQL.");
            }

            // 2. Seed a test profile for development if empty
            if (userRepo.count() == 0) {
                User testUser = new User("israel@sentinel.com", "Abolarin Israel", 4000.0, "Is69310$"); // "$2a$10$9X2aG.8FfRmWvEx9fT3NHe5hE9lT5VexF92CjKux9k9C1Vwxe7p46"
                userRepo.save(testUser);
                System.out.println("👤 Database Seeder: Default profile created. Test User ID: " + testUser.getId());
            }
        };
    }
}