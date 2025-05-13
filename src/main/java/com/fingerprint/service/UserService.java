package com.fingerprint.service;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.User;
import com.fingerprint.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getOrCreateUser(Fingerprint visitorId) {
        return userRepository.findByVisitorId(visitorId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setVisitorId(visitorId);
                    return userRepository.save(newUser);
                });
    }
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
