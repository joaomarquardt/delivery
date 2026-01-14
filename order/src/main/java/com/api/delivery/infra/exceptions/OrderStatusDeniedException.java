package com.api.delivery.infra.exceptions;

public class OrderStatusDeniedException extends RuntimeException {
    public OrderStatusDeniedException(String message) {
        super(message);
    }
}
