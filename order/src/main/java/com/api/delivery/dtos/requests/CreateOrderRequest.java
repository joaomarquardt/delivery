package com.api.delivery.dtos.requests;

import com.api.delivery.enums.PaymentChannel;
import com.api.delivery.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotNull
        List<OrderItemRequest> items,
        @NotNull
        PaymentMethod paymentMethod,
        @NotNull
        PaymentChannel paymentChannel,
        String cardToken
) {

}
