package com.arka.catalog_service.domain.model.GlobalExceptions;

public class ProductException extends RuntimeException {
    public ProductException(String message) {
        super(message);
    }
}
