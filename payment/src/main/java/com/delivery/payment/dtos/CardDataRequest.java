package com.delivery.payment.dtos;

public record CardDataRequest(
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv
) {
}
