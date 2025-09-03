package com.cashbox.moneywallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Set<String> roles;
}
