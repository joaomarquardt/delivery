package com.delivery.payment.infra.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestErrorResponse {
    private int status;
    private String error;
    private Map<String, String> errors;
    private Map<String, Object> details;

    public RestErrorResponse(int status, String error) {
        this.status = status;
        this.error = error;
    }

    public RestErrorResponse(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
