package com.cashbox.moneywallet.authservice.controller;

import com.cashbox.moneywallet.authservice.dto.AuthResponse;
import com.cashbox.moneywallet.authservice.dto.LoginRequest;
import com.cashbox.moneywallet.authservice.dto.RegisterRequest;
import com.cashbox.moneywallet.authservice.service.AuthService;
import com.cashbox.moneywallet.common.response.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
