package com.api.delivery.dtos.responses;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        BigDecimal price,
        int amount
) {
}
