package com.fingerprint.controller;

import com.fingerprint.dto.LoginRequest;
import com.fingerprint.dto.LoginResponse;
import com.fingerprint.dto.RegisterRequest;
import com.fingerprint.entity.Fingerprint;
import com.fingerprint.entity.Role;
import com.fingerprint.entity.User;
import com.fingerprint.repository.FingerprintRepository;
import com.fingerprint.repository.RoleRepository;
import com.fingerprint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final FingerprintRepository fingerprintRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String visitorIdValue = request.getVisitorId();

        // Проверка: такой email уже занят?
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        // Получить Fingerprint
        Optional<Fingerprint> optionalFingerprint = fingerprintRepository.findByVisitorId(visitorIdValue);
        Fingerprint fingerprint;
        if (optionalFingerprint.isEmpty()) {
            // ✅ Создаем новый Fingerprint
            fingerprint = new Fingerprint();
            fingerprint.setVisitorId(visitorIdValue);
            fingerprint = fingerprintRepository.save(fingerprint);
        } else {
            fingerprint = optionalFingerprint.get();
        }

        // Найти пользователя по Fingerprint
        Optional<User> existingUserOpt = userRepository.findByVisitorId(fingerprint);

        User user;
        if (existingUserOpt.isPresent()) {
            // Обновить существующего пользователя
            user = existingUserOpt.get();
        } else {
            // ✅ Создать нового пользователя
            user = new User();
            user.setVisitorId(fingerprint);
        }

        // Обновить поля
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Назначить роль
        Role role = roleRepository.findByName("user");
        if (role == null) {
            throw new RuntimeException("Role 'user' not found");
        }
        user.setRole(role);

        // ✅ Сохранить пользователя
        userRepository.save(user);

        return ResponseEntity.ok("User registered");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        if (!auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Получаем пользователя по email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Собираем ответ
        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getVisitorId() != null ? user.getVisitorId().getVisitorId() : null
        );

        return ResponseEntity.ok(response);
    }
}
