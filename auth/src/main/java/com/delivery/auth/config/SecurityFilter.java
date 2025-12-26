package com.delivery.auth.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.delivery.auth.repositories.UserRepository;
import com.delivery.auth.services.TokenCacheService;
import com.delivery.auth.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final TokenCacheService tokenCacheService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, TokenCacheService tokenCacheService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.tokenCacheService = tokenCacheService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            try {
                String subject = tokenService.validateToken(token);
                if (!tokenCacheService.isTokenValid(token)) {
                    throw new IllegalArgumentException("Invalid or expired token!");
                }
                UserDetails user = userRepository.findByEmail(subject)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + subject));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JWTVerificationException | UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token: " + e.getMessage());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    protected String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        return null;
    }
}
