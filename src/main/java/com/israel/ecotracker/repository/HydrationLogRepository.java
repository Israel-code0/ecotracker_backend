package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.HydrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface HydrationLogRepository extends JpaRepository<HydrationLog, Long> {

    @Query("SELECT SUM(h.amount) FROM HydrationLog h WHERE h.userId = :userId AND h.logDate = :date")
    Integer sumWaterByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);

    void deleteByUserIdAndLogDate(String userId, LocalDate logdate);
}