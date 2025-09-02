package com.cashbox.moneywallet.authservice.service;

import com.cashbox.moneywallet.authservice.dto.response.UserResponse;
import com.cashbox.moneywallet.authservice.entity.User;
import com.cashbox.moneywallet.authservice.exception.UserErrorCodes;
import com.cashbox.moneywallet.authservice.exception.UserManagementException;
import com.cashbox.moneywallet.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse updateUserProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserManagementException(UserErrorCodes.USER_NOT_FOUND));

        validateEmailUniqueness(user, request.getEmail());
        validateCurrentPassword(user, request.getCurrentPassword());

        updateUserFields(user, request);
        User updatedUser = userRepository.save(user);

        return mapToUserResponse(updatedUser);
    }

    private void validateEmailUniqueness(User currentUser, String newEmail) {
        if (!currentUser.getEmail().equals(newEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new UserManagementException(UserErrorCodes.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserManagementException(UserErrorCodes.CURRENT_PASSWORD_MISMATCH);
        }
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        if (!user.getEmail().equals(request.getEmail())) {
            user.setEmail(request.getEmail());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .kycVerified(user.isKycVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}