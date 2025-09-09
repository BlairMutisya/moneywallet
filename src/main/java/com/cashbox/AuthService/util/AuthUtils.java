package com.cashbox.AuthService.util;

import com.cashbox.AuthService.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final JwtService jwtService;

    public String getPhoneFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    public boolean isTokenExpired(String token) {
        return jwtService.isTokenExpired(token);
    }
}
