package com.api.delivery.dtos.requests;

import com.api.delivery.domain.ProductType;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        BigDecimal price,
        String description,
        ProductType productType
) {
}
