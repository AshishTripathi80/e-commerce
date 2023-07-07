package com.productservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorMsg;
    private List<String> errors;

    public ErrorResponse(String errorMsg, LocalDateTime timestamp, List<String> errors) {
        this.timestamp = timestamp;
        this.errorMsg = errorMsg;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public List<String> getErrors() {
        return errors;
    }
}
