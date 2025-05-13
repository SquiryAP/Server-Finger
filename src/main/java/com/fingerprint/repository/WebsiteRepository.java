package com.fingerprint.repository;

import com.fingerprint.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebsiteRepository extends JpaRepository<Website, Long> {
    Optional<Website> findByDomain(String domain);
}
