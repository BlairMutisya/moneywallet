package com.cashbox.moneywallet.authservice.dto.response;

import com.cashbox.moneywallet.authservice.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
    private boolean enabled;
    private boolean kycVerified;
    private Instant createdAt;
    private Instant updatedAt;
}