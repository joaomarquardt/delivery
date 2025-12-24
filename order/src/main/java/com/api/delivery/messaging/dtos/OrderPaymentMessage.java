package com.api.delivery.messaging.dtos;

import com.api.delivery.enums.PaymentChannel;
import com.api.delivery.enums.PaymentMethod;

import java.math.BigDecimal;

public record OrderPaymentMessage(
        Long orderId,
        PaymentMethod paymentMethod,
        PaymentChannel paymentChannel,
        BigDecimal value,
        String cardToken
) {
}
