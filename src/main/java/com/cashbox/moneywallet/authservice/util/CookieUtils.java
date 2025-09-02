package com.cashbox.moneywallet.authservice.util;


import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public class CookieUtils {
    public static ResponseCookie createSecureCookie(String name, String value, long maxAgeSec) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAgeSec)
                .sameSite("Strict")
                .build();
    }

    public static Cookie createHttpOnlyCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}