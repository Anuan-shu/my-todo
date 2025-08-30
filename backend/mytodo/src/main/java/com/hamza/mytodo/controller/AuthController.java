package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.user.LoginRequest;
import com.hamza.mytodo.dto.user.RegisterRequest;
import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
        } else {
            response.put("success", false);
            response.put("message", "用户名已存在");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("username", user.getUsername());
            response.put("userId", user.getId());
        } else {
            response.put("success", false);
            response.put("message", "用户名或密码错误");
        }
        
        return ResponseEntity.ok(response);
    }
} 