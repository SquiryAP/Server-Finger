package com.fingerprint.repository;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByWebsiteIdIn(List<Long> websiteIds);
    List<Log> findByUserLastKnownIpOrUserVisitorId(String ip, Fingerprint visitorId);
}
