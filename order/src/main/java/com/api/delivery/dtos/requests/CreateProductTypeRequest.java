package com.api.delivery.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record CreateProductTypeRequest(
        @NotBlank(message = "Name is required and cannot be empty")
        String name
) {
}
