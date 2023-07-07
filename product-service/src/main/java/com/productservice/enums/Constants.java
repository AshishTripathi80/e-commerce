package com.productservice.enums;

public enum Constants {

    ERROR_PRODUCT_NOT_FOUND("Product not found with ID: "),
    ERROR_INVALID_PRODUCT_DATA("Invalid product data: "),

    ;

    private final String message;

    Constants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
