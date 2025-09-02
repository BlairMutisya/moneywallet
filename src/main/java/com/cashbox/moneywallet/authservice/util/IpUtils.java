package com.cashbox.moneywallet.authservice.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class IpUtils {
    public static String getClientIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(header -> header.split(",")[0])
                .orElse(request.getRemoteAddr());
    }
}