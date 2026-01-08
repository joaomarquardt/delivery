package com.delivery.auth.dtos.responses;

import com.delivery.auth.enums.UserRole;

public record UserResponse(
        Long id,
        String name,
        String email,
        String password,
        UserRole userRole
) {
}