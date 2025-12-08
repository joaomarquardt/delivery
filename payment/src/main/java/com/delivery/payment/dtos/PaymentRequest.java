package com.delivery.payment.dtos;

import com.delivery.payment.domain.PaymentMethod;
import com.delivery.payment.enums.PaymentChannel;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        PaymentMethod paymentMethod,
        PaymentChannel paymentChannel,
        BigDecimal value,
        String cardToken
) {
}
