package com.example.insectspopularscience.controller;

import com.example.insectspopularscience.dto.ApiResponse;
import com.example.insectspopularscience.dto.AuthResponse;
import com.example.insectspopularscience.dto.LoginRequest;
import com.example.insectspopularscience.dto.RegisterRequest;
import com.example.insectspopularscience.dto.UpdateProfileRequest;
import com.example.insectspopularscience.dto.ChangePasswordRequest;
import com.example.insectspopularscience.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ApiResponse.success("注册成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ApiResponse<com.example.insectspopularscience.entity.User> getCurrentUser() {
        try {
            com.example.insectspopularscience.entity.User user = authService.getCurrentUser();
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/profile")
    public ApiResponse<com.example.insectspopularscience.entity.User> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        try {
            com.example.insectspopularscience.entity.User user = authService.updateProfile(request);
            return ApiResponse.success("修改成功", user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            authService.changePassword(request);
            return ApiResponse.success("密码修改成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}

