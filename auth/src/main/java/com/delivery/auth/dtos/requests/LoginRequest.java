package com.delivery.auth.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required and cannot be blank")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required and cannot be blank")
        String password
) {
}
