package com.fingerprint.controller;

import com.fingerprint.entity.Website;
import com.fingerprint.repository.WebsiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/websites")
@RequiredArgsConstructor
public class WebsiteController {

    private final WebsiteRepository websiteRepository;

    @GetMapping
    public List<Website> getAllWebsites() {
        return websiteRepository.findAll();
    }

    @PostMapping
    public Website addWebsite(@RequestBody Website website) {
        return websiteRepository.save(website);
    }
}
