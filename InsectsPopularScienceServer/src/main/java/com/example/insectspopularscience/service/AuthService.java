package com.example.insectspopularscience.service;

import com.example.insectspopularscience.dto.AuthResponse;
import com.example.insectspopularscience.dto.LoginRequest;
import com.example.insectspopularscience.dto.RegisterRequest;
import com.example.insectspopularscience.entity.User;
import com.example.insectspopularscience.repository.UserRepository;
import com.example.insectspopularscience.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getNickname());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        return new AuthResponse(token, "Bearer", user.getId(), user.getUsername(), user.getNickname());
    }

    public User getCurrentUser() {
        Long userId = com.example.insectspopularscience.security.UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Transactional
    public User updateProfile(com.example.insectspopularscience.dto.UpdateProfileRequest request) {
        Long userId = com.example.insectspopularscience.security.UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
            user.setNickname(request.getNickname());
        }
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (userRepository.existsByEmail(request.getEmail()) && 
                !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("邮箱已被使用");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAvatar() != null && !request.getAvatar().trim().isEmpty()) {
            user.setAvatar(request.getAvatar());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(com.example.insectspopularscience.dto.ChangePasswordRequest request) {
        Long userId = com.example.insectspopularscience.security.UserPrincipal.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

