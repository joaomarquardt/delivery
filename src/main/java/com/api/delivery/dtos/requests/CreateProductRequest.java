package com.api.delivery.dtos.requests;


import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        BigDecimal price,
        String description,
        Long productTypeId
) {
}
