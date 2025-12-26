package com.delivery.auth.dtos.requests;

public record CreateUserRequest(
        String name,
        String email,
        String password
) {
}
