package com.productservice.exceptions;

public class ErrorMessages {
    public enum ErrorMessage {
        PRODUCT_NOT_FOUND("Product not found with id: ");

        private final String message;

        ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
