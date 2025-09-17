package com.cashbox.AuthService.service.impl;

import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.dto.request.*;
import com.cashbox.AuthService.dto.response.AuthResponse;
import com.cashbox.AuthService.dto.response.JwtResponse;
import com.cashbox.AuthService.dto.response.RegisterResponse;
import com.cashbox.AuthService.entity.AccountType;
import com.cashbox.AuthService.entity.RefreshToken;
import com.cashbox.AuthService.entity.User;
import com.cashbox.AuthService.enums.Role;
import com.cashbox.AuthService.events.UserRegisteredEvent;
import com.cashbox.AuthService.events.producer.UserEventProducer;
import com.cashbox.AuthService.exception.InvalidTokenException;
import com.cashbox.AuthService.exception.UserAlreadyExistsException;
import com.cashbox.AuthService.exception.UserNotFoundException;
import com.cashbox.AuthService.repository.AccountTypeRepository;
import com.cashbox.AuthService.repository.RefreshTokenRepository;
import com.cashbox.AuthService.repository.UserRepository;
import com.cashbox.AuthService.security.JwtService;
import com.cashbox.AuthService.service.AuthService;
import com.cashbox.AuthService.service.EmailService;
import com.cashbox.AuthService.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final OtpGenerator otpGenerator;
    private final UserEventProducer userEventProducer;

    @Override
    public BaseApiResponse<RegisterResponse> register(RegisterRequest request) {
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserAlreadyExistsException("Phone number already in use");
        }

        AccountType accountType = accountTypeRepository
                .findById(request.getAccountTypeId())
                .orElseThrow(() -> new RuntimeException("Invalid account type"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountType(accountType)
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);

        userRepository.save(user);

        // Publish Kafka event
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .build();
        userEventProducer.publishUserRegistered(event);

        RegisterResponse response = RegisterResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();

        return BaseApiResponse.success("Registration successful", response);
    }

    @Override
    public BaseApiResponse<JwtResponse> login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refresh = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusMillis(jwtService.getAllClaims(refreshToken).getExpiration().getTime()))
                .build();
//        refreshTokenRepository.save(refresh);

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();

        return BaseApiResponse.success("Login successful", jwtResponse);
    }

    @Override
    public BaseApiResponse<JwtResponse> refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(token.getUser());

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token.getToken())
                .userId(token.getUser().getId())
                .firstName(token.getUser().getFirstName())
                .middleName(token.getUser().getMiddleName())
                .lastName(token.getUser().getLastName())
                .email(token.getUser().getEmail())
                .phone(token.getUser().getPhone())
                .roles(token.getUser().getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();

        return BaseApiResponse.success("Token refreshed", jwtResponse);
    }

    @Override
    public BaseApiResponse<Void> logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
        return BaseApiResponse.success("Logged out successfully");
    }

    @Override
    public BaseApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String otp = otpGenerator.generate6DigitOtp();
        // persist OTP in Redis or DB

        emailService.sendOtp(user.getEmail(), otp);
        return BaseApiResponse.success("OTP sent to email");
    }

    @Override
    public BaseApiResponse<Void> resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return BaseApiResponse.success("Password reset successful");
    }
}
