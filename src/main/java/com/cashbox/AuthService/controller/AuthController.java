package com.cashbox.AuthService.controller;

import com.cashbox.AuthService.service.AuthService;
import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.dto.request.*;
import com.cashbox.AuthService.dto.response.AuthResponse;
import com.cashbox.AuthService.dto.response.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication and account management")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with email, phone, and password")
    @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    public BaseApiResponse<AuthResponse> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a user and return JWT tokens")
    @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    public BaseApiResponse<JwtResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh JWT token", description = "Generate a new access token using a valid refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
            content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    public BaseApiResponse<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidate the refresh token for the logged-in user")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    public BaseApiResponse<Void> logout(@RequestParam String refreshToken) {
        return authService.logout(refreshToken);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Send OTP to user’s registered email for password reset")
    @ApiResponse(responseCode = "200", description = "OTP sent successfully")
    public BaseApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset user’s password using OTP and new password")
    @ApiResponse(responseCode = "200", description = "Password reset successful")
    public BaseApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }
}
