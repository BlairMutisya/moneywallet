package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.constants.AuthMessages;
import com.cashbox.moneywallet.authservice.dto.request.*;
import com.cashbox.moneywallet.authservice.dto.response.AuthResponse;
import com.cashbox.moneywallet.authservice.dto.response.BaseApiResponse;
import com.cashbox.moneywallet.authservice.dto.response.JwtResponse;
import com.cashbox.moneywallet.authservice.entity.RefreshToken;
import com.cashbox.moneywallet.authservice.entity.Role;
import com.cashbox.moneywallet.authservice.entity.User;
import com.cashbox.moneywallet.authservice.exception.AuthErrorCodes;
import com.cashbox.moneywallet.authservice.exception.AuthException;
import com.cashbox.moneywallet.authservice.repository.RefreshTokenRepository;
import com.cashbox.moneywallet.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

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