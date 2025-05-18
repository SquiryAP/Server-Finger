package com.fingerprint.controller;

import com.fingerprint.dto.CollectFingerprintDTO;
import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.User;
import com.fingerprint.entity.IPAddress;
import com.fingerprint.entity.Website;
import com.fingerprint.repository.WebsiteRepository;
import com.fingerprint.service.FingerprintService;
import com.fingerprint.service.IPAddressService;
import com.fingerprint.service.LogService;
import com.fingerprint.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/fingerprint")
@RequiredArgsConstructor
public class FingerprintController {

    private final UserService userService;
    private final FingerprintService fingerprintService;
    private final LogService logService;
    private final IPAddressService ipAddressService;
    private final WebsiteRepository websiteRepository;

    @PostMapping("/collect")
    @Transactional
    public String collectFingerprint(@RequestBody CollectFingerprintDTO data, HttpServletRequest request) {
        String visitorId = data.getVisitorId();
        String fingerprintJson = data.getFingerprintJson();
        Website website = data.getWebsite();
        Long timeSpentSeconds = Long.parseLong(data.getTimeSpentSeconds().toString());

        if (visitorId == null || fingerprintJson == null || website == null || website.getDomain() == null || timeSpentSeconds == null) {
            return "Invalid data";
        }

        // 1. Проверка существующего отпечатка или создание нового
        Fingerprint fingerprint = fingerprintService.findByVisitorId(visitorId)
                .orElseGet(() -> fingerprintService.trySave(visitorId, fingerprintJson));

        // 2. Создать или получить пользователя по отпечатку
        User user = userService.getOrCreateUser(fingerprint);

        // 3. Найти сайт по домену или сохранить новый
        Website savedWebsite = websiteRepository.findByDomain(website.getDomain())
                .orElseGet(() -> websiteRepository.save(website));

        // 4. Сохранить IP и его геолокацию
        String clientIp = getClientIp(request);
        IPAddress ipAddress = ipAddressService.saveOrUpdateIP(user, clientIp);

        // 5. Сохранить лог захода
        logService.saveLog(user, savedWebsite, timeSpentSeconds, ipAddress);

        return "Fingerprint collected successfully";
    }


    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
