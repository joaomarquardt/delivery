package com.api.delivery.dtos.requests;

public record OrderItemRequest(
        Long productId,
        int amount
) {
}
