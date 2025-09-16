package com.cashbox.AuthService.util;

import com.cashbox.AuthService.entity.User;
import com.cashbox.AuthService.repository.UserRepository;
import com.cashbox.AuthService.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String getPhoneFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    public boolean isTokenExpired(String token) {
        return jwtService.isTokenExpired(token);
    }

    public User getCurrentUser() {
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

}
