package com.api.delivery.dtos.requests;

import com.api.delivery.domain.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus orderStatus
) {
}
