package com.delivery.auth.dtos.requests;

public record UpdateUserRequest(
        String name,
        String email,
        String password
) {
}