package com.api.delivery.dtos.requests;

import java.math.BigDecimal;

public record OrderPaymentMessage(
        Long orderId,
        String paymentMethod,
        String paymentChannel,
        BigDecimal value,
        String cardToken
) {
}
