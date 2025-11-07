package com.arka.catalog_service.domain.model.GlobalExceptions;

public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String message) {
        super(message);
    }
}
