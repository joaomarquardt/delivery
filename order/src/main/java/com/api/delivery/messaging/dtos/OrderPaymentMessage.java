package com.api.delivery.messaging.dtos;

import java.math.BigDecimal;

public record OrderPaymentMessage(
        Long orderId,
        String paymentMethod,
        String paymentChannel,
        BigDecimal value,
        String cardToken
) {
}
