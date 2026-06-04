package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.Badge;
import com.israel.ecotracker.domain.EarnedBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BadgeRepository extends JpaRepository<EarnedBadge, String> {
    List<EarnedBadge> findByUserId(String userId);
    boolean existsByUserIdAndBadgeCode(String userId, String badgeCode);
}