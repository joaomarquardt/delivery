package com.api.delivery.infra.exceptions;

public class InvalidOrderStatusTransitionExceptio extends RuntimeException {
    public InvalidOrderStatusTransitionExceptio(String message) {
        super(message);
    }
}
