package com.delivery.payment.dtos;

import com.delivery.payment.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentMessage(
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal value,
        String cardToken
) {
}
