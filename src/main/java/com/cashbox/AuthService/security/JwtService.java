package com.cashbox.AuthService.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;
    private final long refreshTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpiration,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.jwtExpiration = jwtExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // =====================
    // Token Generation for phone
    // =====================
    public String generateToken(String phone) {
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String phone) {
        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getRefreshTokenDuration() {
        return refreshTokenExpiration / 1000; // duration in seconds
    }

    // =====================
    // Token Generation for UserDetails
    // =====================
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // =====================
    // Token Validation
    // =====================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // =====================
    // Token Parsing
    // =====================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
//package com.cashbox.AuthService.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//public class JwtService {
//
//    private final SecretKey secretKey;
//    private final long jwtExpiration;
//    private final long refreshTokenExpiration;
//
//    public JwtService(
//            @Value("${jwt.secret}") String secret,
//            @Value("${jwt.expiration}") long jwtExpiration,
//            @Value("${jwt.refresh-expiration}") long refreshTokenExpiration
//    ) {
//        // Ensure the secret is properly base64 encoded and has sufficient length
//        byte[] keyBytes = Base64.getDecoder().decode(secret);
//        if (keyBytes.length < 32) {
//            // Pad to 256 bits for HS256
//            keyBytes = Arrays.copyOf(keyBytes, 32);
//        }
//        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
//        this.jwtExpiration = jwtExpiration;
//        this.refreshTokenExpiration = refreshTokenExpiration;
//    }
//
//    // =====================
//    // Token Generation for User entity
//    // =====================
//    public String generateToken(com.cashbox.AuthService.entity.User user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", user.getId());
//        claims.put("phone", user.getPhone());
//        claims.put("email", user.getEmail());
////        claims.put("roles", user.getAuthorities().stream()
////                .map(GrantedAuthority::getAuthority)
////                .collect(Collectors.toList()));
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getId().toString()) // User ID as subject
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String generateRefreshToken(com.cashbox.AuthService.entity.User user) {
//        return Jwts.builder()
//                .setSubject(user.getId().toString()) // User ID as subject
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // =====================
//    // Token Generation for UserDetails (keep for compatibility)
//    // =====================
//    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(userDetails, new HashMap<>());
//    }
//
//    // =====================
//    // Token Validation
//    // =====================
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    public boolean isTokenValid(String token, com.cashbox.AuthService.entity.User user) {
//        final Long userId = extractUserId(token);
//        return userId.equals(user.getId()) && !isTokenExpired(token);
//    }
//
//    public boolean isTokenValid(String token) {
//        try {
//            extractAllClaims(token);
//            return !isTokenExpired(token);
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    public boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // =====================
//    // Token Parsing - Enhanced for microservices
//    // =====================
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Long extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        // Try to get userId from claims, fallback to subject
//        Long userId = claims.get("userId", Long.class);
//        if (userId == null) {
//            // For backward compatibility, try to parse subject
//            try {
//                userId = Long.valueOf(claims.getSubject());
//            } catch (NumberFormatException e) {
//                throw new JwtException("Invalid user ID in token");
//            }
//        }
//        return userId;
//    }
//
//    public String extractPhone(String token) {
//        return extractClaim(token, claims -> claims.get("phone", String.class));
//    }
//
//    public String extractEmail(String token) {
//        return extractClaim(token, claims -> claims.get("email", String.class));
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<String> extractRoles(String token) {
//        return extractClaim(token, claims -> claims.get("roles", List.class));
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(secretKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            throw new JwtException("Token expired", e);
//        } catch (UnsupportedJwtException e) {
//            throw new JwtException("Token unsupported", e);
//        } catch (MalformedJwtException e) {
//            throw new JwtException("Token malformed", e);
//        } catch (SecurityException e) {
//            throw new JwtException("Invalid signature", e);
//        } catch (IllegalArgumentException e) {
//            throw new JwtException("Token empty or null", e);
//        }
//    }
//
//    public long getRefreshTokenDuration() {
//        return refreshTokenExpiration / 1000; // duration in seconds
//    }
//
//    public long getJwtExpiration() {
//        return jwtExpiration;
//    }
//}