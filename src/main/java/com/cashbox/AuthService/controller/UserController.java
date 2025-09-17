package com.cashbox.AuthService.controller;

import com.cashbox.AuthService.dto.request.UserUpdateRequest;
import com.cashbox.AuthService.dto.response.UserResponse;
import com.cashbox.AuthService.service.UserService;
import com.cashbox.AuthService.common.BaseApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user by their unique ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
    )
    public BaseApiResponse<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        return BaseApiResponse.success("User found", userService.getUserById(id));
    }

    public BaseApiResponse<UserResponse> getUserByPhone(
            @Parameter(description = "User phone number", required = true)
            @PathVariable String phone) {
        return BaseApiResponse.success("User found", userService.getUserByPhone(phone));
    }
    @Operation(
            summary = "Check if phone number exists",
            description = "Used by mobile app to determine whether to show login or registration page"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Returns true if phone exists, false otherwise"
    )
    @GetMapping("/exists/phone/{phone}")
    public BaseApiResponse<Boolean> checkPhoneExists(@PathVariable String phone) {
        boolean exists = userService.existsByPhone(phone);
        return BaseApiResponse.success(
                "Phone existence checked successfully",
                exists
        );
    }


    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get user by email",
            description = "Retrieve a user by their email address"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
    )
    public BaseApiResponse<UserResponse> getUserByEmail(
            @Parameter(description = "User email address", required = true)
            @PathVariable String email) {
        return BaseApiResponse.success("User found", userService.getUserByEmail(email));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user",
            description = "Update user details by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
    )
    public BaseApiResponse<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
            )
            @RequestBody UserUpdateRequest request) {
        return BaseApiResponse.success("User updated successfully", userService.updateUser(id, request));
    }

    @PutMapping("/{id}/account-type")
    @Operation(
            summary = "Link account type",
            description = "Assign or update a user’s account type"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Account type linked successfully",
            content = @Content(schema = @Schema(implementation = BaseApiResponse.class))
    )
    public BaseApiResponse<Void> linkAccountType(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Account type ID to link", required = true)
            @RequestParam Long accountTypeId) {
        userService.linkAccountType(id, accountTypeId);
        return BaseApiResponse.success("Account type linked successfully");
    }
}
