package com.orderservice.handler;

import com.orderservice.exception.ErrorResponse;
import com.orderservice.exception.InvalidOrderDataException;
import com.orderservice.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidOrderDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderDataException(InvalidOrderDataException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Validation Failed!", ex.getTimestamp(), ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFoundException(OrderNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
