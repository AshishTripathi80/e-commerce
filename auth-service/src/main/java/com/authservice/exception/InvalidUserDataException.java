package com.authservice.exception;

import java.time.LocalDateTime;
import java.util.List;

public class InvalidUserDataException extends RuntimeException {
    private LocalDateTime timestamp;
    private List<String> errors;

    public InvalidUserDataException(String message, LocalDateTime localDateTime, List<String> errors) {
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
