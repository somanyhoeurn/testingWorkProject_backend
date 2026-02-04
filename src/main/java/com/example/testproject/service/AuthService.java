package com.example.testproject.service;

import com.example.testproject.dto.request.AuthRequest;
import com.example.testproject.dto.request.RegisterRequest;
import com.example.testproject.dto.response.AuthResponse;
import com.example.testproject.dto.response.RegisterResponse;
import com.example.testproject.dto.response.UserResponse;

public interface AuthService {
    AuthResponse<UserResponse> login(AuthRequest authRequest);

    RegisterResponse register(RegisterRequest registerRequest);
}
