package com.delivery.auth.dtos.requests;

public record LoginRequest(
        String email,
        String password
) {
}
