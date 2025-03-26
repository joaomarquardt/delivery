package com.demo.delivery.services;

import com.demo.delivery.domain.User;
import com.demo.delivery.domain.UserRole;
import com.demo.delivery.dtos.LoginRequestDTO;
import com.demo.delivery.dtos.RegisterRequestDTO;
import com.demo.delivery.exceptions.RegisterConflictException;
import com.demo.delivery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public void register(RegisterRequestDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.email()).isPresent()) {
            throw new RegisterConflictException("Email is already in use");
        }
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        User user = new User(registerDTO.name(), registerDTO.email(), encryptedPassword, UserRole.BASIC);
        userRepository.save(user);
    }

    public String login(LoginRequestDTO loginDTO) {
        userRepository.findByEmail(loginDTO.email()).orElseThrow(() -> new UsernameNotFoundException("User not found by email"));
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            return tokenService.generateToken((User) auth.getPrincipal());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

    }
}
