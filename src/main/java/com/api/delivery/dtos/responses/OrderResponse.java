package com.api.delivery.dtos.responses;

import com.api.delivery.domain.OrderStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        Timestamp orderedOn,
        OrderStatus status,
        BigDecimal totalValue,
        List<OrderItemResponse> items
) {
}
