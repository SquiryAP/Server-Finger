package com.fingerprint.service;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.repository.FingerprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FingerprintService {
    private final FingerprintRepository fingerprintRepository;

    public Fingerprint saveFingerprint(String visitorId, String fingerprintJson) {
        if (!fingerprintRepository.findByVisitorId(visitorId).isPresent()) {
            Fingerprint fp = new Fingerprint();
            fp.setVisitorId(visitorId);
            fp.setHashComponents(fingerprintJson);
            fingerprintRepository.save(fp);
            return fp;
        }
        return null;
    }
}
