package com.authservice.handler;

import com.authservice.exception.ErrorResponse;
import com.authservice.exception.InvalidUserDataException;
import com.authservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCustomerDataException(InvalidUserDataException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Validation Failed!", ex.getTimestamp(), ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
