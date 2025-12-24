package com.delivery.payment.infra.exceptions;

public class InvalidPaymentChannelException extends RuntimeException {
    public InvalidPaymentChannelException(String message) {
        super(message);
    }
}
