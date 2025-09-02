package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.constants.AuthMessages;
import com.cashbox.moneywallet.authservice.dto.request.LoginRequest;
import com.cashbox.moneywallet.authservice.dto.request.RegisterRequest;
import com.cashbox.moneywallet.authservice.dto.response.AuthResponse;
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
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(AuthErrorCodes.EMAIL_EXISTS);
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .mustChangePassword(false)
                .enabled(true)
                .kycVerified(false)
                .build();

        userRepository.save(user);

        return generateAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCodes.INVALID_CREDENTIALS);
        }

        var user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new AuthException(AuthErrorCodes.INVALID_CREDENTIALS));

        if (!user.isEnabled()) {
            throw new AuthException(AuthErrorCodes.ACCOUNT_DISABLED);
        }

        // Revoke all existing refresh tokens
        refreshTokenRepository.revokeAllByUser(user.getId());

        return generateAuthResponse(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private AuthResponse generateAuthResponse(User user) {
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveRefreshToken(User user, String token) {
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }
}