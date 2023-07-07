package com.productservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public class InvalidProductDataException extends RuntimeException {
    private LocalDateTime timestamp;
    private List<String> errors;

    public InvalidProductDataException(String message, LocalDateTime localDateTime, List<String> errors) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }
}
