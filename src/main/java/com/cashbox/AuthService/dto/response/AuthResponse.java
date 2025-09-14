package com.cashbox.AuthService.dto.response;

import com.cashbox.AuthService.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String firstName;
    private String middleName;
    private String lastName;
    private Set<Role> roles;
}