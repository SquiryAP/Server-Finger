package com.fingerprint.repository;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByVisitorId(Fingerprint visitorId);
    Optional<User> findByEmail(String email);
}
