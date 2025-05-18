package com.fingerprint.controller;

import com.fingerprint.dto.LogDto;
import com.fingerprint.entity.*;
import com.fingerprint.repository.*;

import com.fingerprint.service.IPAddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FingerprintRepository fingerprintRepository;

    @Autowired
    private UserWebsiteRepository userWebsiteRepository;

    private  IPAddressService ipAddressService;

    @Autowired
    private IPAddressRepository ipAddressRepository;

    private LogDto toDto(Log log) {
        User user = log.getUser();
        Fingerprint fingerprint = user.getVisitorId();
        IPAddress ipAddressEntity = log.getIpAddress();
        LogDto.IpDto ipDto = null;
        if (ipAddressEntity != null) {
            ipDto = new LogDto.IpDto(
                    ipAddressEntity.getIp(),
                    ipAddressEntity.getCountry(),
                    ipAddressEntity.getCity(),
                    ipAddressEntity.getFirstSeen(),
                    ipAddressEntity.getLastSeen()
            );
        }
        // Безопасно обрабатываем возможный null
        LogDto.WebsiteDto websiteDto = null;
        if (log.getWebsite() != null) {
            websiteDto = new LogDto.WebsiteDto(
                    log.getWebsite().getDomain()
            );
        }

        return new LogDto(
                log.getId(),
                log.getVisitTime(),
                Math.toIntExact(log.getTimeSpentSeconds()),
                new LogDto.UserDto(
                        user.getId(),
                        user.getRole() != null ? user.getRole().getName() : null,
                        fingerprint != null ? fingerprint.getVisitorId() : null
                ),
                websiteDto,
                ipDto
        );
    }


    @GetMapping("/logs")
    public List<LogDto> getLogs(@RequestParam String visitorId, HttpServletRequest request) {
        Fingerprint fingerprint = fingerprintRepository.findByVisitorId(visitorId)
                .orElseThrow(() -> new RuntimeException("Fingerprint not found"));
        User user = userRepository.findByVisitorId(fingerprint)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole().getName();
        List<Log> logs;

        if (role.equals("admin") || role.equals("gos")) {
            logs = logRepository.findAll();
        } else if (role.equals("owner")) {
            List<Long> websiteIds = userWebsiteRepository
                    .findByUserIdAndVerifiedTrue(user.getId())
                    .stream()
                    .map(uw -> uw.getWebsite().getId())
                    .collect(Collectors.toList());
            logs = logRepository.findByWebsiteIdIn(websiteIds);
        } else {
            String clientIp = getClientIp(request);
            IPAddress ipAddress = ipAddressRepository.findByIp(clientIp);
            logs = logRepository.findByUserVisitorIdOrIpAddress(fingerprint, ipAddress);
        }

        return logs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

