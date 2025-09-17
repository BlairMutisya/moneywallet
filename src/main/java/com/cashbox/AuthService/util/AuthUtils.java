package com.cashbox.AuthService.util;

import com.cashbox.AuthService.entity.User;
import com.cashbox.AuthService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    private Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        throw new IllegalStateException("User not authenticated");
    }

    public String getPhoneFromJwt() {
        return getCurrentJwt().getClaim("phone");
    }

    public Long getUserIdFromJwt() {
        return getCurrentJwt().getClaim("userId");
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromJwt() {
        List<String> roles = getCurrentJwt().getClaim("roles");
        return Set.copyOf(roles);
    }

    public User getCurrentUser() {
        String phone = getPhoneFromJwt();
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
