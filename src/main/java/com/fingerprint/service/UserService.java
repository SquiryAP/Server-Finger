package com.fingerprint.service;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.Role;
import com.fingerprint.entity.User;
import com.fingerprint.repository.RoleRepository;
import com.fingerprint.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User getOrCreateUser(Fingerprint visitorId) {
        return userRepository.findByVisitorId(visitorId)
                .orElseGet(() -> {
                    Role userRole = roleRepository.findByName("user");
                    User newUser = new User();
                    newUser.setVisitorId(visitorId);
                    newUser.setRole(userRole);
                    return userRepository.save(newUser);
                });
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
