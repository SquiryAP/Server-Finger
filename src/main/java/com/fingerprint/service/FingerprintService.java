package com.fingerprint.service;

import com.fingerprint.entity.Fingerprint;
import com.fingerprint.repository.FingerprintRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FingerprintService {
    private final FingerprintRepository fingerprintRepository;

    @Transactional
    public Fingerprint findOrCreate(String visitorId, String fingerprintJson) {
        return fingerprintRepository.findByVisitorId(visitorId)
                .orElseGet(() -> trySave(visitorId, fingerprintJson));
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW) // для Jakarta EE
    public Fingerprint trySave(String visitorId, String fingerprintJson) {
        try {
            Fingerprint fingerprint = new Fingerprint();
            fingerprint.setVisitorId(visitorId);
            fingerprint.setHashComponents(fingerprintJson);
            return fingerprintRepository.save(fingerprint);
        } catch (DataIntegrityViolationException e) {
            // В параллельной транзакции уже вставили
            return fingerprintRepository.findByVisitorId(visitorId)
                    .orElseThrow(() -> e);
        }
    }


    public Optional<Fingerprint> findByVisitorId(String visitorId) {
        return fingerprintRepository.findByVisitorId(visitorId);
    }
}
