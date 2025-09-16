package com.cashbox.AuthService.service;

import com.cashbox.AuthService.dto.request.*;
import com.cashbox.AuthService.dto.response.AuthResponse;
import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.dto.response.JwtResponse;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public interface AuthService {
    BaseApiResponse<AuthResponse> register(RegisterRequest request);
    BaseApiResponse<JwtResponse> login(LoginRequest request);
    BaseApiResponse<JwtResponse> refreshToken(RefreshTokenRequest request);
    BaseApiResponse<Void> logout(String refreshToken);
    BaseApiResponse<Void> forgotPassword(ForgotPasswordRequest request);
    BaseApiResponse<Void> resetPassword(ResetPasswordRequest request);

}