package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.dto.request.UserUpdateRequest;
import com.cashbox.moneywallet.authservice.dto.response.BaseApiResponse;
import com.cashbox.moneywallet.authservice.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    /**
     * Get user by ID.
     */
    BaseApiResponse<UserResponse> getUserById(Long userId);

    /**
     * Get all users.
     */
    BaseApiResponse<List<UserResponse>> getAllUsers();

    /**
     * Update user details.
     */
    BaseApiResponse<UserResponse> updateUser(Long userId, UserUpdateRequest request);

    /**
     * Delete a user (soft delete recommended).
     */
    BaseApiResponse<Void> deleteUser(Long userId);

    /**
     * Assign a role to a user.
     */
    BaseApiResponse<UserResponse> assignRole(Long userId, String roleName);

    /**
     * Assign an account type to a user.
     */
    BaseApiResponse<UserResponse> assignAccountType(Long userId, String accountTypeName);

}
