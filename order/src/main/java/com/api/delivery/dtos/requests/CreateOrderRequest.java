package com.api.delivery.dtos.requests;

import com.api.delivery.enums.PaymentChannel;
import com.api.delivery.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "The items list cannot be null")
        @NotEmpty(message = "The order must have at least one item")
        List<OrderItemRequest> items,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,
        @NotNull(message = "Payment channel is required")
        PaymentChannel paymentChannel,
        String cardToken
) {
}
