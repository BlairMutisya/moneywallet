package com.cashbox.moneywallet.authservice.dto.response;

import java.util.Set;

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
