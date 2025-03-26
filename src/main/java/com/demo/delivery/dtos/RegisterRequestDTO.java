package com.demo.delivery.dtos;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {
}
