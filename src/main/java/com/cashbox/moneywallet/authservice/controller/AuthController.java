package com.cashbox.moneywallet.authservice.controller;

import com.cashbox.moneywallet.authservice.constants.AuthMessages;
import com.cashbox.moneywallet.authservice.dto.request.LoginRequest;
import com.cashbox.moneywallet.authservice.dto.request.RegisterRequest;
import com.cashbox.moneywallet.authservice.dto.response.AuthResponse;
import com.cashbox.moneywallet.authservice.exception.AuthErrorCodes;
import com.cashbox.moneywallet.authservice.exception.AuthException;
import com.cashbox.moneywallet.authservice.service.AuthService;
import com.cashbox.moneywallet.common.response.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", description = "Registration successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    public ResponseEntity<BaseApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(
                BaseApiResponse.success(AuthMessages.REGISTER_SUCCESS, response)
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Authentication failed")
    public ResponseEntity<BaseApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(
                    BaseApiResponse.success(AuthMessages.LOGIN_SUCCESS, response)
            );
        } catch (AuthException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseApiResponse.failure(ex.getErrorCode(), ex.getMessage()));
        }
    }

//    @PostMapping("/logout")
//    @Operation(summary = "Logout user")
//    public ResponseEntity<BaseApiResponse<Void>> logout() {
//        authService.logout();
//        return ResponseEntity.ok(
//                BaseApiResponse.success(AuthMessages.LOGOUT_SUCCESS, null)
//        );
//    }
}