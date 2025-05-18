package com.fingerprint.dto;

import com.fingerprint.entity.Website;

public class CollectFingerprintDTO {
    private String visitorId;
    private String fingerprintJson;
    private Website website;
    private Long timeSpentSeconds;

    // Геттеры и сеттеры
    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getFingerprintJson() {
        return fingerprintJson;
    }

    public void setFingerprintJson(String fingerprintJson) {
        this.fingerprintJson = fingerprintJson;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(Long timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }
}
