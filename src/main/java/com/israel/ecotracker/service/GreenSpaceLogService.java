package com.israel.ecotracker.service;

import com.israel.ecotracker.domain.GreenSpaceLog;
import com.israel.ecotracker.repository.GreenSpaceLogRepository;
import org.springframework.stereotype.Service;

@Service
public class GreenSpaceLogService {

    private final GreenSpaceLogRepository greenSpaceLogRepository;

    public GreenSpaceLogService(GreenSpaceLogRepository greenSpaceRepository) {
        this.greenSpaceLogRepository = greenSpaceRepository;
    }

    public void logSession(String userId, int minutes) {
        GreenSpaceLog session = new GreenSpaceLog(userId, minutes, java.time.LocalDate.now());
        greenSpaceLogRepository.save(session);
    }

    public int getTotalMinutes(String userId) {
        return greenSpaceLogRepository.sumTotalMinutesByUserId(userId);
    }
}