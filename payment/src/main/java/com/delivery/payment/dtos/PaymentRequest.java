package com.delivery.payment.dtos;

import com.delivery.payment.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal value,
        CardDataRequest cardData
) {
}
