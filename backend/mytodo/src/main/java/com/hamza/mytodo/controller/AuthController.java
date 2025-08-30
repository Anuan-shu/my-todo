package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.UserDto;
import com.hamza.mytodo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowCredentials = "true")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDto.RegisterRequest request, HttpSession session) {
        boolean success = authService.register(request.getUsername(), request.getPassword(), session);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("username", request.getUsername());
        } else {
            response.put("success", false);
            response.put("message", "用户名已存在");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto.LoginRequest request, HttpSession session) {
        boolean success = authService.login(request.getUsername(), request.getPassword(), session);
        
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("username", request.getUsername());
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        authService.logout(session);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "退出成功");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        boolean isLoggedIn = authService.isLoggedIn(session);
        
        Map<String, Object> response = new HashMap<>();
        response.put("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            response.put("username", authService.getCurrentUsername(session));
        }
        
        return ResponseEntity.ok(response);
    }
} 