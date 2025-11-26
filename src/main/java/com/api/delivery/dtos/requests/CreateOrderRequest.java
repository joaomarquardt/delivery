package com.api.delivery.dtos.requests;

import java.util.List;

public record CreateOrderRequest(
        List<OrderItemRequest> items
) {

}
