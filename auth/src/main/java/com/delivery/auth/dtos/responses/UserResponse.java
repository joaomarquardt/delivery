package com.delivery.auth.dtos.responses;

public record UserResponse(
        Long id,
        String name,
        String email,
        String password
) {
}