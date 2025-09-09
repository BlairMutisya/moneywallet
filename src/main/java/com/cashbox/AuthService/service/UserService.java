package com.cashbox.AuthService.service;

import com.cashbox.AuthService.dto.request.UserUpdateRequest;
import com.cashbox.AuthService.dto.response.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getUserByPhone(String phone);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void linkAccountType(Long userId, Long accountTypeId);

}
