package com.fingerprint.repository;

import com.fingerprint.entity.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {
}
