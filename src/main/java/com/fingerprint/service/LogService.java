package com.fingerprint.service;

import com.fingerprint.entity.Log;
import com.fingerprint.entity.User;
import com.fingerprint.entity.Website;
import com.fingerprint.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void saveLog(User user, Website website, Long timeSpentSeconds) {
        Log log = new Log();
        log.setUser(user);
        log.setWebsite(website);
        log.setVisitTime(LocalDateTime.now());
        log.setTimeSpentSeconds(timeSpentSeconds);
        logRepository.save(log);
    }
}
