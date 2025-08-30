package com.hamza.mytodo.dto.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
