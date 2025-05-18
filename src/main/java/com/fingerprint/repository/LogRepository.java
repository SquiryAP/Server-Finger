package com.fingerprint.repository;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.IPAddress;
import com.fingerprint.entity.Log;
import com.fingerprint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByWebsiteIdIn(List<Long> websiteIds);
    List<Log> findByUserVisitorId(Fingerprint visitorId);
    List<Log> findByUserVisitorIdOrIpAddress(Fingerprint visitorId, IPAddress ipAddress);
}
