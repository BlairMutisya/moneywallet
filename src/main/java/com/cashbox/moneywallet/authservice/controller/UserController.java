package com.cashbox.moneywallet.authservice.controller;

import com.cashbox.moneywallet.authservice.dto.response.UserResponse;
import com.cashbox.moneywallet.authservice.entity.User;
import com.cashbox.moneywallet.authservice.service.UserService;
import com.cashbox.moneywallet.common.response.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<BaseApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal User currentUser) {
        UserResponse response = userService.getUserProfile(currentUser.getId());
        return ResponseEntity.ok(BaseApiResponse.success("Profile retrieved", response));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateCurrentUser(
            @AuthenticationPrincipal User currentUser,
            @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUserProfile(currentUser.getId(), request);
        return ResponseEntity.ok(BaseApiResponse.success("Profile updated", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users (Admin only)")
    public ResponseEntity<BaseApiResponse<Page<UserResponse>>> getAllUsers(
            Pageable pageable,
            @RequestParam(required = false) String search) {
        Page<UserResponse> users = userService.getAllUsers(pageable, search);
        return ResponseEntity.ok(BaseApiResponse.success("Users retrieved", users));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<BaseApiResponse<UserResponse>> getUserById(
            @PathVariable Long userId) {
        UserResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(BaseApiResponse.success("User retrieved", response));
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user status (Admin only)")
    public ResponseEntity<BaseApiResponse<Void>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean enabled) {
        userService.updateUserStatus(userId, enabled);
        return ResponseEntity.ok(BaseApiResponse.success("User status updated"));
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user role (Admin only)")
    public ResponseEntity<BaseApiResponse<Void>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return ResponseEntity.ok(BaseApiResponse.success("User role updated"));
    }

}