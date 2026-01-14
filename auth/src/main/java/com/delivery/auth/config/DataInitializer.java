package com.delivery.auth.config;

import com.delivery.auth.domain.User;
import com.delivery.auth.enums.UserRole;
import com.delivery.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Value("${api.security.admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@admin.com");
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setUserRole(UserRole.ADMIN);
                userRepository.save(admin);
                System.out.println("User ADMIN created successfully!");
            }
        };
    }
}
