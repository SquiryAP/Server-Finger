package com.fingerprint.repository;

import com.fingerprint.entity.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {
    Optional<Fingerprint> findByVisitorId(String visitorId);
}
