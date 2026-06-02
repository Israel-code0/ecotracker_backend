package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.GreenSpaceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GreenSpaceLogRepository extends JpaRepository<GreenSpaceLog, Long> {

    // Calculates the lifetime total of mindful minutes for a specific user
    @Query("SELECT COALESCE(SUM(g.minutes), 0) FROM GreenSpaceLog g WHERE g.userId = :userId")
    int sumTotalMinutesByUserId(@Param("userId") String userId);
}