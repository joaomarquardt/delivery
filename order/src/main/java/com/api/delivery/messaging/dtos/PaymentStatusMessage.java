package com.api.delivery.messaging.dtos;

public record PaymentStatusMessage(
        Long orderId,
        boolean success
) {
}
