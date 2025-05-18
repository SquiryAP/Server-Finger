package com.fingerprint.dto;

import com.fingerprint.entity.IPAddress;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LogDto {
    private Long id;
    private LocalDateTime visitTime;
    private int timeSpentSeconds;
    private UserDto user;
    private WebsiteDto website;
    private IpDto ipAddress;

    @Data
    @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String role;
        private String visitorId;
    }

    @Data
    @AllArgsConstructor
    public static class IpDto {
        private String ip;
        private String country;
        private String city;
        private LocalDateTime firstSeen;
        private LocalDateTime lastSeen;
    }

    @Data
    @AllArgsConstructor
    public static class WebsiteDto {
        private String domain;
    }

}