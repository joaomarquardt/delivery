package com.api.delivery.dtos.requests;

import java.math.BigDecimal;

public record CreateOrderItemRequest(
        Long productId,
        BigDecimal price,
        int amount
) {
}
