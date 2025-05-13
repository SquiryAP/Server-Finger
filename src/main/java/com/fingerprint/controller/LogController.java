package com.fingerprint.controller;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.Log;
import com.fingerprint.entity.User;

import com.fingerprint.repository.LogRepository;
import com.fingerprint.repository.UserWebsiteRepository;
import com.fingerprint.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserWebsiteRepository userWebsiteRepository;

    @GetMapping("/logs")
    public List<Log> getLogs(Authentication authentication) {
        User currentUser = userService.getByEmail(authentication.getName());
        String role = currentUser.getRole().getName();

        if (role.equals("admin")) {
            return logRepository.findAll();
        } else if (role.equals("owner")) {
            List<Long> websiteIds = userWebsiteRepository
                    .findByUserIdAndVerifiedTrue(currentUser.getId())
                    .stream()
                    .map(uw -> uw.getWebsite().getId())
                    .collect(Collectors.toList());
            return logRepository.findByWebsiteIdIn(websiteIds);
        } else {
             String ip = currentUser.getLastKnownIp();
             Fingerprint visitorId = currentUser.getVisitorId();
            return logRepository.findByUserLastKnownIpOrUserVisitorId(ip, visitorId);
        }
    }
}