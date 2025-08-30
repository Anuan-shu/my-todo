package com.hamza.mytodo.dto;

import lombok.Data;

public class UserDto {
    
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
    
    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
    }
    
    @Data
    public static class AuthResponse {
        private String token;
        private String username;
    }
} 