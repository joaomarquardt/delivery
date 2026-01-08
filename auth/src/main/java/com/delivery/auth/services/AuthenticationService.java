package com.delivery.auth.services;

import com.delivery.auth.domain.User;
import com.delivery.auth.dtos.requests.CreateUserRequest;
import com.delivery.auth.dtos.requests.LoginRequest;
import com.delivery.auth.dtos.requests.RegisterRequest;
import com.delivery.auth.dtos.responses.TokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenCacheService tokenCacheService;


    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService, TokenCacheService tokenCacheService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.tokenCacheService = tokenCacheService;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        User user = userService.findUserEntityByEmail(loginRequest.email());
        var userPassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        try {
            var auth = authenticationManager.authenticate(userPassword);
            User userAuth = (User) auth.getPrincipal();
            String token = tokenService.generateToken(userAuth);
            return new TokenResponse(token);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public void register(RegisterRequest registerRequest) {
        if (!registerRequest.password().equals(registerRequest.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userService.existsUserByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        String encryptedPassword = passwordEncoder.encode(registerRequest.password());
        CreateUserRequest createUser = new CreateUserRequest(registerRequest.name(), registerRequest.email(), encryptedPassword, registerRequest.userRole());
        userService.createUser(createUser);
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }
        String cleanToken = token.replace("Bearer ", "");
        tokenCacheService.invalidateToken(cleanToken);
    }
}
