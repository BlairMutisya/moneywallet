//package com.cashbox.AuthService.security;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//    private final UserDetailsServiceImpl userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String jwt = authHeader.substring(7);
//
//        // extract the subject (phone) for loading user details
//        String phone;
//        try {
//            phone = jwtService.extractUsername(jwt);
//        } catch (Exception e) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Load user from DB
//            UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
//
//            // Validate token
//            if (!jwtService.isTokenExpired(jwt)) {
//
//                // (Optional) build authorities from claims instead of DB:
//                Claims claims = jwtService.getAllClaims(jwt);
//                @SuppressWarnings("unchecked")
//                List<String> roles = (List<String>) claims.get("roles");
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails, // principal
//                                null,
//                                roles == null
//                                        ? userDetails.getAuthorities()
//                                        : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
//                        );
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
