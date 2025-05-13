package com.fingerprint.repository;

import com.fingerprint.entity.UserWebsite;
import com.fingerprint.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWebsiteRepository extends JpaRepository<UserWebsite, Long> {
    List<UserWebsite> findByUserIdAndVerifiedTrue(Long userId);
}
