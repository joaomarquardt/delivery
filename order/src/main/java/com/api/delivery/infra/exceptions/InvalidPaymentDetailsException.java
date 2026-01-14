package com.api.delivery.infra.exceptions;

public class InvalidPaymentDetailsException extends RuntimeException {
    public InvalidPaymentDetailsException(String message) {
        super(message);
    }
}
