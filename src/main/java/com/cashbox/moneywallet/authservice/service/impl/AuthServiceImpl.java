package com.cashbox.moneywallet.authservice.service.impl;

import com.cashbox.moneywallet.authservice.config.JwtService;
import com.cashbox.moneywallet.authservice.dto.AuthResponse;
import com.cashbox.moneywallet.authservice.dto.LoginRequest;
import com.cashbox.moneywallet.authservice.dto.RegisterRequest;
import com.cashbox.moneywallet.authservice.entity.Role;
import com.cashbox.moneywallet.authservice.entity.User;
import com.cashbox.moneywallet.authservice.exception.BadRequestException;
import com.cashbox.moneywallet.authservice.exception.NotFoundException;
import com.cashbox.moneywallet.authservice.repository.UserRepository;
import com.cashbox.moneywallet.authservice.service.AuthService;
import com.cashbox.moneywallet.common.response.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public BaseApiResponse<AuthResponse> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .mustChangePassword(true)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return BaseApiResponse.success("User registered successfully",
                AuthResponse.builder()
                        .accessToken(token)
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build());
    }


    @Override
    public BaseApiResponse<AuthResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new BadRequestException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return BaseApiResponse.success("Login successful",
                AuthResponse.builder()
                        .accessToken(token)
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build());
    }

}
