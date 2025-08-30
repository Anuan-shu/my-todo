package com.hamza.mytodo.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
