package com.api.delivery.dtos.requests;

import com.api.delivery.enums.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus orderStatus
) {
}
