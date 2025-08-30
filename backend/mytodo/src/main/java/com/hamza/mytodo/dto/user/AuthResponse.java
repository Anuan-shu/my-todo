package com.hamza.mytodo.dto.user;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
}
