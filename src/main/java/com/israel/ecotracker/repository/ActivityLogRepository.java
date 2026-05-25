package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.ActivityLog;
import com.israel.ecotracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    // Retrieve all activities logged by a specific user, sorted newest first
    List<ActivityLog> findByUserIdOrderByLoggedAtDesc(UUID userId);
}