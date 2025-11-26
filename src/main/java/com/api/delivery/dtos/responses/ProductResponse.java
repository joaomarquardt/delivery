package com.api.delivery.dtos.responses;

import com.api.delivery.domain.ProductType;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String description,
        ProductType productType
) {
}
