package com.api.delivery.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductTypeRequest(
        @NotBlank(message = "Name is required and cannot be empty")
        String name
) {
}
