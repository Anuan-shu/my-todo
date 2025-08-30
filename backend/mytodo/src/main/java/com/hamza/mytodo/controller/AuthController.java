package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.UserDto;
import com.hamza.mytodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<UserDto.AuthResponse> register(@RequestBody UserDto.RegisterRequest request) {
        UserDto.AuthResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserDto.AuthResponse> login(@RequestBody UserDto.LoginRequest request) {
        UserDto.AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
} 