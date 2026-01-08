package com.delivery.auth.dtos.requests;

import com.delivery.auth.enums.UserRole;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        UserRole userRole
) {
}
