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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public SecurityFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Role");
        if (userId != null && userRole != null) {
            String roleWithPrefix = userRole.startsWith("ROLE_") ? userRole : "ROLE_" + userRole;
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleWithPrefix));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
