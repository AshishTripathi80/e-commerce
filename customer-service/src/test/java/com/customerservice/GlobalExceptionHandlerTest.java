package com.customerservice;

import com.customerservice.exceptions.CustomerNotFoundException;
import com.customerservice.exceptions.ErrorResponse;
import com.customerservice.exceptions.InvalidCustomerDataException;
import com.customerservice.exceptions.UserAlreadyExistsException;
import com.customerservice.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void handleInvalidCustomerDataException_ShouldReturnErrorResponse() {
        // Arrange
        InvalidCustomerDataException exception = new InvalidCustomerDataException("Validation Failed!",
                LocalDateTime.now(), Arrays.asList("Field 1: Error 1", "Field 2: Error 2"));

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleInvalidCustomerDataException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertEquals(exception.getTimestamp(), errorResponse.getTimestamp());
        List<String> errors = errorResponse.getErrors();
        assertEquals(2, errors.size());
        assertEquals("Field 1: Error 1", errors.get(0));
        assertEquals("Field 2: Error 2", errors.get(1));
    }

    @Test
    void handleUserAlreadyExistsException_ShouldReturnErrorMessage() {
        // Arrange
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // Act
        ResponseEntity<String> response = handler.handleUserAlreadyExistsException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void handleCustomerNotFoundException_ShouldReturnErrorMessage() {
        // Arrange
        CustomerNotFoundException exception = new CustomerNotFoundException("Customer not found");

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        // Act
        ResponseEntity<String> response = handler.handleCustomerNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Customer not found", response.getBody());
    }
}
