package com.fingerprint.controller;

import com.fingerprint.dto.UpdateUserRoleRequest;
import com.fingerprint.entity.Role;
import com.fingerprint.entity.User;
import com.fingerprint.repository.RoleRepository;
import com.fingerprint.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // предполагается, что у тебя есть репозиторий ролей

    // Получить список всех пользователей
    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Обновить роль пользователя по ID
    @PutMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request) {

        return userRepository.findById(id)
                .map(user -> {
                    Role role = roleRepository.findByName(request.getRoleName());
                    if (role == null) {
                        throw new RuntimeException("Role not found");
                    }
                    user.setRole(role);
                    User updated = userRepository.save(user);
                    return ResponseEntity.ok(toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Преобразование User в DTO (создай свой UserDto с нужными полями)
    public  UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole().getName()
        );
    }

    // DTO для User (создай отдельный класс или вложенный)
    @Data
    @AllArgsConstructor
    public  static class UserDto {
        private Long id;
        private String email;
        private String roleName;
    }
}
