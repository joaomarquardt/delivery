package com.delivery.payment.dtos;

public record PaymentStatusMessage(
        Long orderId,
        boolean success
) {
}
