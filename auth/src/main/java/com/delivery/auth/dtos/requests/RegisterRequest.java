package com.delivery.auth.dtos.requests;

import com.delivery.auth.enums.UserRole;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String passwordConfirmation,
        UserRole userRole
) {
}
