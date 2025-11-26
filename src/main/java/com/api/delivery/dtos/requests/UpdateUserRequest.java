package com.api.delivery.dtos.requests;

public record UpdateUserRequest(
        String name,
        String email,
        String password
) {
}
