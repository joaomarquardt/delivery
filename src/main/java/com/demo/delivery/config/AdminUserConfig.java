package com.demo.delivery.config;

import com.demo.delivery.domain.User;
import com.demo.delivery.domain.UserRole;
import com.demo.delivery.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminUserConfig {
    @Value("${admin.secret}")
    private String secret;

    @Bean
    public CommandLineRunner loadUserAdmin(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User("Administrador", "admin@admin.com", passwordEncoder.encode(secret), UserRole.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
