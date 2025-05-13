package com.fingerprint.controller;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.User;
import com.fingerprint.entity.Website;
import com.fingerprint.service.FingerprintService;
import com.fingerprint.service.IPAddressService;
import com.fingerprint.service.LogService;
import com.fingerprint.service.UserService;
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

    @PostMapping("/collect")
    public String collectFingerprint(@RequestBody Map<String, Object> data, HttpServletRequest request) {
        String visitorId = (String) data.get("visitorId");
        String fingerprintJson = (String) data.get("fingerprintJson");
        Website website = (Website) data.get("website");
        Long timeSpentSeconds = Long.parseLong(data.get("timeSpentSeconds").toString());

        if (visitorId == null || fingerprintJson == null || website == null || timeSpentSeconds == null) {
            return "Invalid data";
        }



        // 2. Сохранить отпечаток (если ещё не сохранён)
        Fingerprint fingerprint = fingerprintService.saveFingerprint(visitorId, fingerprintJson);

        // 1. Создать или получить пользователя
        User user = userService.getOrCreateUser(fingerprint);
        // 3. Сохранить лог захода
        logService.saveLog(user, website, timeSpentSeconds);

        // 4. Сохранить IP и его геолокацию
        String clientIp = getClientIp(request);
        ipAddressService.saveOrUpdateIP(user, clientIp);

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
