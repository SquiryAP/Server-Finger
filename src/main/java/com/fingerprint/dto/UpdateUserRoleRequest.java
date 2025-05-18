package com.fingerprint.dto;

import lombok.Data;

@Data
public class UpdateUserRoleRequest {
    private String roleName; // например, "admin", "user", "owner"
}