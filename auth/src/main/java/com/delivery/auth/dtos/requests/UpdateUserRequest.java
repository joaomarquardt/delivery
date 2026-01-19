package com.delivery.auth.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "Name is required and cannot be blank")
        String name,
        @NotBlank(message = "Email is required and cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required and cannot be blank")
        String password
) {
}