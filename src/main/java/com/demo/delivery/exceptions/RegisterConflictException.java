package com.demo.delivery.exceptions;

public class RegisterConflictException extends RuntimeException {
    public RegisterConflictException(String message) {
        super(message);
    }
}
