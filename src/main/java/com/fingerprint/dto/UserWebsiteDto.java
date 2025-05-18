package com.fingerprint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWebsiteDto {
    private Long id;
    private SimpleUserDto user;
    private SimpleWebsiteDto website;
    private Boolean verified;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleUserDto {
        private Long id;
        private String email;  // или другое поле, по которому удобно идентифицировать
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleWebsiteDto {
        private Long id;
        private String domain;
    }
}