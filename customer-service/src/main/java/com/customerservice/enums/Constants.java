package com.customerservice.enums;

public enum Constants {
    ERROR_USER_ALREADY_EXISTS("User already exists with email: "),
    ERROR_CUSTOMER_NOT_FOUND("Customer not found with ID: "),
    ;

    private final String message;

    Constants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
