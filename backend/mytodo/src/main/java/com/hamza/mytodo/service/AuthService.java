package com.hamza.mytodo.service;

import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 简单的密码验证（实际项目中应该使用加密）
            if (password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }
    
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 实际项目中应该加密
        
        return userRepository.save(user);
    }
} 