package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.dto.AuthResponse;
import com.cashbox.moneywallet.authservice.dto.LoginRequest;
import com.cashbox.moneywallet.authservice.dto.RegisterRequest;
import com.cashbox.moneywallet.common.response.BaseApiResponse;

public interface AuthService {
    BaseApiResponse<AuthResponse> register(RegisterRequest request);
    BaseApiResponse<AuthResponse> login(LoginRequest request);
}
