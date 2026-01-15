package com.delivery.auth.infra.handler;

import com.delivery.auth.infra.exceptions.AuthenticationFailedException;
import com.delivery.auth.infra.exceptions.DifferentPasswordsException;
import com.delivery.auth.infra.exceptions.EmailAlreadyExistsException;
import com.delivery.auth.infra.exceptions.InvalidTokenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        RestErrorResponse response = new RestErrorResponse(status.value(), errors);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({
            AuthenticationFailedException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<RestErrorResponse> handleUnauthorizedExceptions(RuntimeException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        RestErrorResponse response = new RestErrorResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DifferentPasswordsException.class)
    public ResponseEntity<RestErrorResponse> handleBadRequestExceptions(DifferentPasswordsException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestErrorResponse response = new RestErrorResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<RestErrorResponse> handleConflictExceptions(EmailAlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        RestErrorResponse response = new RestErrorResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestErrorResponse> handleNotFoundExceptions(EntityNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        RestErrorResponse response = new RestErrorResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorResponse> handleForbiddenExceptions(AccessDeniedException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        RestErrorResponse response = new RestErrorResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> handleGeneralExceptions(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        RestErrorResponse response = new RestErrorResponse(status.value(), "An unexpected error occurred");
        return ResponseEntity.status(status).body(response);
    }
}
