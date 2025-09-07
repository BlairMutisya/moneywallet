package com.cashbox.moneywallet.service;

import com.cashbox.moneywallet.dto.request.UserUpdateRequest;
import com.cashbox.moneywallet.dto.response.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getUserByPhone(String phone);

    UserResponse getUserByEmail(String email);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void linkAccountType(Long userId, Long accountTypeId);

}
