package com.fingerprint.controller;

import com.fingerprint.dto.LogDto;
import com.fingerprint.dto.UserWebsiteDto;
import com.fingerprint.dto.VerifiedUpdateRequest;
import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.User;
import com.fingerprint.entity.Website;
import com.fingerprint.entity.UserWebsite;
import com.fingerprint.repository.FingerprintRepository;
import com.fingerprint.repository.UserRepository;
import com.fingerprint.repository.WebsiteRepository;
import com.fingerprint.repository.UserWebsiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-websites")
@RequiredArgsConstructor
public class UserWebsiteController {

    private final FingerprintRepository fingerprintRepository;
    private final UserRepository userRepository;
    private final WebsiteRepository websiteRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    UserWebsiteDto toDto(UserWebsite uw) {
        UserWebsiteDto.SimpleUserDto userDto = new UserWebsiteDto.SimpleUserDto(
                uw.getUser().getId(),
                uw.getUser().getEmail()  // или другое поле
        );

        UserWebsiteDto.SimpleWebsiteDto websiteDto = new UserWebsiteDto.SimpleWebsiteDto(
                uw.getWebsite().getId(),
                uw.getWebsite().getDomain()
        );

        return new UserWebsiteDto(
                uw.getId(),
                userDto,
                websiteDto,
                uw.getVerified()
        );
    }
    @PostMapping("/assign")
    public String assignWebsitesToUser(
            @RequestParam String email,
            @RequestBody List<String> websiteDomains
    ) {


        // 2. Получить User по Fingerprint
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Получить все Website по доменам
        List<Website> websites = websiteRepository.findAllByDomainIn(websiteDomains);

        // 4. Создать связи user-website
        for (Website website : websites) {
            UserWebsite userWebsite = new UserWebsite();
            userWebsite.setUser(user);
            userWebsite.setWebsite(website);
            userWebsite.setVerified(false); // по условию
            userWebsiteRepository.save(userWebsite);
        }

        return "Websites assigned to user with verified = false";
    }
    @GetMapping
    public List<UserWebsiteDto> getAllUserWebsites() {
        List<UserWebsite> userWebsites = userWebsiteRepository.findAll();
        return userWebsites.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVerified(
            @PathVariable Long id,
            @RequestBody VerifiedUpdateRequest request) {

        return userWebsiteRepository.findById(id)
                .map(userWebsite -> {
                    userWebsite.setVerified(request.getVerified());
                    UserWebsite updated = userWebsiteRepository.save(userWebsite);
                    UserWebsiteDto dto = toDto(updated);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
