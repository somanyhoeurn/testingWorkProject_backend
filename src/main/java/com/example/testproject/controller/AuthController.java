package com.example.testproject.controller;

import com.example.testproject.Base.BaseController;
import com.example.testproject.dto.request.AuthRequest;
import com.example.testproject.dto.request.RegisterRequest;
import com.example.testproject.dto.response.ApiResponse;
import com.example.testproject.dto.response.AuthResponse;
import com.example.testproject.dto.response.RegisterResponse;
import com.example.testproject.dto.response.UserResponse;
import com.example.testproject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse<UserResponse>>> login(@Valid @RequestBody AuthRequest authRequest){
        return response("LOGIN_SUCCESS","Login successfully.",authService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest registerRequest){
        return response("REGISTER_SUCCESS","Register successful.",authService.register(registerRequest));
    }

}
