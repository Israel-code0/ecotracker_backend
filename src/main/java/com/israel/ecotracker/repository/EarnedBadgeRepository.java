package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.EarnedBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarnedBadgeRepository extends JpaRepository<EarnedBadge, Long> {

    // Finds all badges a user has unlocked
    List<EarnedBadge> findByUserId(String userId);

    // Checks if a user already owns a specific badge
    boolean existsByUserIdAndBadgeCode(String userId, String badgeCode);
}