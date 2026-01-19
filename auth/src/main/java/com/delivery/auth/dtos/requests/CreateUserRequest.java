package com.delivery.auth.dtos.requests;

import com.delivery.auth.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank(message = "Name is required and cannot be blank")
        String name,
        @NotBlank(message = "Email is required and cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required and cannot be blank")
        String password,
        @NotNull(message = "User role is required")
        UserRole userRole
) {
}
