package com.cashbox.AuthService.security;

import com.cashbox.AuthService.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}") // e.g. 900000 = 15 min
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        return buildToken(user, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenExpiration);
    }

    private String buildToken(User user, long expiryMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("phone", user.getPhone());
        claims.put("roles", user.getRoles().stream().map(Enum::name).toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getPhone()) // subject still the phone
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }
}
