package com.fingerprint.repository;

import com.fingerprint.entity.IPAddress;
import com.fingerprint.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {
    IPAddress findByIp(String ip);
}
