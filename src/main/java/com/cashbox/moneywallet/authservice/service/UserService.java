package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.dto.request.UpdateUserRequest;
import com.cashbox.moneywallet.authservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse getUserProfile(Long userId);
    UserResponse updateUserProfile(Long userId, UpdateUserRequest request);
    Page<UserResponse> getAllUsers(Pageable pageable, String search);
    void updateUserStatus(Long userId, boolean enabled);
    void updateUserRole(Long userId, String role);
}