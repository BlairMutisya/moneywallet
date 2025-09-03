package com.cashbox.moneywallet.service;

import com.cashbox.moneywallet.dto.request.*;
import com.cashbox.moneywallet.dto.response.AuthResponse;
import com.cashbox.moneywallet.common.BaseApiResponse;
import com.cashbox.moneywallet.dto.response.JwtResponse;
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