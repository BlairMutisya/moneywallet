package com.cashbox.moneywallet.controller;

import com.cashbox.moneywallet.dto.request.UserUpdateRequest;
import com.cashbox.moneywallet.dto.response.UserResponse;
import com.cashbox.moneywallet.enums.AccountType;
import com.cashbox.moneywallet.service.UserService;
import com.cashbox.moneywallet.common.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public BaseApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return BaseApiResponse.success(userService.getUserById(id), "User found");
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "Get user by phone", description = "Retrieve a user by their phone number")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public BaseApiResponse<UserResponse> getUserByPhone(@PathVariable String phone) {
        return BaseApiResponse.success(userService.getUserByPhone(phone), "User found");
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a user by their email")
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public BaseApiResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        return BaseApiResponse.success(userService.getUserByEmail(email), "User found");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user details by ID")
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
    )
    public BaseApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
    ) {
        return BaseApiResponse.success(userService.updateUser(id, request), "User updated successfully");
    }

    @PutMapping("/{id}/account-type")
    @Operation(summary = "Link account type", description = "Assign or update a user’s account type")
    @ApiResponse(responseCode = "200", description = "Account type linked successfully")
    public BaseApiResponse<Void> linkAccountType(
            @PathVariable Long id,
            @RequestParam AccountType accountType
    ) {
        userService.linkAccountType(id, accountType);
        return BaseApiResponse.success(null, "Account type linked successfully");
    }
}
