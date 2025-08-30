package com.hamza.mytodo.service;

import com.hamza.mytodo.dto.UserDto;
import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.repository.UserRepository;
import com.hamza.mytodo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    public UserDto.AuthResponse register(UserDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getUsername());
        return new UserDto.AuthResponse(token, user.getUsername());
    }
    
    public UserDto.AuthResponse login(UserDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String token = jwtUtil.generateToken(request.getUsername());
        return new UserDto.AuthResponse(token, request.getUsername());
    }
} 