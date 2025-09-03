package com.cashbox.moneywallet.service.impl;

import com.cashbox.moneywallet.common.BaseApiResponse;
import com.cashbox.moneywallet.enums.AccountType;
import com.cashbox.moneywallet.enums.Role;
import com.cashbox.moneywallet.exception.InvalidAccountTypeException;
import com.cashbox.moneywallet.security.JwtService;
import com.cashbox.moneywallet.service.EmailService;
import com.cashbox.moneywallet.service.AuthService;
import com.cashbox.moneywallet.util.OtpGenerator;
import com.cashbox.moneywallet.util.ResponseUtils;
import com.cashbox.moneywallet.dto.request.*;
import com.cashbox.moneywallet.dto.response.AuthResponse;
import com.cashbox.moneywallet.dto.response.JwtResponse;
import com.cashbox.moneywallet.entity.RefreshToken;
import com.cashbox.moneywallet.entity.User;
import com.cashbox.moneywallet.exception.InvalidTokenException;
import com.cashbox.moneywallet.exception.UserAlreadyExistsException;
import com.cashbox.moneywallet.exception.UserNotFoundException;
import com.cashbox.moneywallet.repository.RefreshTokenRepository;
import com.cashbox.moneywallet.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final OtpGenerator otpGenerator;

    @Override
    public BaseApiResponse<AuthResponse> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserAlreadyExistsException("Phone number already in use");
        }

        AccountType accountType = request.getAccountType();
        if (accountType == null) {
            throw new InvalidAccountTypeException("Account type is required");
        }

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

        String accessToken = jwtService.generateToken(user.getPhone());
        String refreshToken = jwtService.generateRefreshToken(user.getPhone());

        RefreshToken refresh = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(jwtService.getRefreshTokenDuration()))
                .build();
        refreshTokenRepository.save(refresh);

        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();

        return ResponseUtils.success(response, "Registration successful");
    }

    @Override
    public BaseApiResponse<JwtResponse> login(LoginRequest request) {
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user.getPhone());
        String refreshToken = jwtService.generateRefreshToken(user.getPhone());

        RefreshToken refresh = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(jwtService.getRefreshTokenDuration()))
                .build();
        refreshTokenRepository.save(refresh);

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();

        return ResponseUtils.success(jwtResponse, "Login successful");
    }

    @Override
    public BaseApiResponse<JwtResponse> refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token expired");
        }

        String newAccessToken = jwtService.generateToken(token.getUser().getPhone());

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(token.getToken())
                .userId(token.getUser().getId())
                .firstName(token.getUser().getFirstName())
                .lastName(token.getUser().getLastName())
                .email(token.getUser().getEmail())
                .phone(token.getUser().getPhone())
                .roles(token.getUser().getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
                .build();

        return ResponseUtils.success(jwtResponse, "Token refreshed");
    }

    @Override
    public BaseApiResponse<Void> logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
        return ResponseUtils.success(null, "Logged out successfully");
    }

    @Override
    public BaseApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {
        // 1. Find user by phone, throw exception if not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 2. Generate 6-digit OTP using your OTP generator utility
        String otp = otpGenerator.generate6DigitOtp();

        // 3. Persist OTP in Redis or DB for later verification
        // TODO: Implement OTP persistence

        // 4. Send OTP to user's email
        emailService.sendOtp(user.getEmail(), otp);

        // 5. Return success response
        return ResponseUtils.success(null, "OTP sent to email");
    }


    @Override
    public BaseApiResponse<Void> resetPassword(ResetPasswordRequest request) {
        // 1. Find user by email is optional if OTP is linked directly to email
        // TODO: Verify OTP from Redis/DB and get corresponding user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

//        String otp = otpGenerator.generate6DigitOtp();

        // 2. Validate new password
        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        // 3. Encode and set new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 4. Return success response
        return ResponseUtils.success(null, "Password reset successful");
    }


}
