package com.delivery.payment.infra.exceptions;

public class IncompatiblePaymentMethodException extends RuntimeException {
    public IncompatiblePaymentMethodException(String message) {
        super(message);
    }
}
