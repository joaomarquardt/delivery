package com.api.delivery.dtos.requests;

public record CreateUserRequest(
        String name,
        String email,
        String password
) {
}
