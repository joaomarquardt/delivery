package com.api.delivery.dtos.requests;

import com.api.delivery.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Order status is required")
        OrderStatus orderStatus
) {
}
