package com.hamza.mytodo.service;

import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    
    public boolean login(String username, String password, HttpSession session) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 简单的密码验证（实际项目中应该使用加密）
            if (password.equals(user.getPassword())) {
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                return true;
            }
        }
        return false;
    }
    
    public boolean register(String username, String password, HttpSession session) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 实际项目中应该加密
        
        User savedUser = userRepository.save(user);
        session.setAttribute("userId", savedUser.getId());
        session.setAttribute("username", savedUser.getUsername());
        return true;
    }
    
    public void logout(HttpSession session) {
        session.invalidate();
    }
    
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
    
    public Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }
    
    public String getCurrentUsername(HttpSession session) {
        return (String) session.getAttribute("username");
    }
} 