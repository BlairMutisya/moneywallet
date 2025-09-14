package com.cashbox.AuthService.service.impl;

import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.entity.AccountType;
import com.cashbox.AuthService.enums.Role;
import com.cashbox.AuthService.events.UserRegisteredEvent;
import com.cashbox.AuthService.events.producer.UserEventProducer;
import com.cashbox.AuthService.repository.AccountTypeRepository;
import com.cashbox.AuthService.security.JwtService;
import com.cashbox.AuthService.service.EmailService;
import com.cashbox.AuthService.service.AuthService;
import com.cashbox.AuthService.util.OtpGenerator;
import com.cashbox.AuthService.util.ResponseUtils;
import com.cashbox.AuthService.dto.request.*;
import com.cashbox.AuthService.dto.response.AuthResponse;
import com.cashbox.AuthService.dto.response.JwtResponse;
import com.cashbox.AuthService.entity.RefreshToken;
import com.cashbox.AuthService.entity.User;
import com.cashbox.AuthService.exception.InvalidTokenException;
import com.cashbox.AuthService.exception.UserAlreadyExistsException;
import com.cashbox.AuthService.exception.UserNotFoundException;
import com.cashbox.AuthService.repository.RefreshTokenRepository;
import com.cashbox.AuthService.repository.UserRepository;
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
    public BaseApiResponse<AuthResponse> register(RegisterRequest request) {
        // 1. Validate unique phone
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new UserAlreadyExistsException("Email already in use");
//        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new UserAlreadyExistsException("Phone number already in use");
        }

        // 2. Find account type
        AccountType accountType = accountTypeRepository
                .findById(request.getAccountTypeId())
                .orElseThrow(() -> new RuntimeException("Invalid account type"));

        // 3. Build user entity
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

        // 4. Assign default role
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);

        // 5. Save user
        userRepository.save(user);

        // 6. Generate JWT tokens
        String accessToken = jwtService.generateToken(user.getPhone());
        String refreshToken = jwtService.generateRefreshToken(user.getPhone());

        // 7. Save refresh token
        RefreshToken refresh = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(jwtService.getRefreshTokenDuration()))
                .build();
        refreshTokenRepository.save(refresh);




   //TODO: Remove the Access Token and

        // 8. Publish Kafka event
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .build();
        userEventProducer.publishUserRegistered(event);







        // 9. Prepare response
        AuthResponse response = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
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
                .middleName(user.getMiddleName())
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
                .middleName(token.getUser().getMiddleName())
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
        // 1. Find user by email
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
