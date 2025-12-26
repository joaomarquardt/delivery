package com.delivery.auth.dtos.requests;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String passwordConfirmation
) {
}
