package com.arka.catalog_service.domain.model.GlobalExceptions;

public class CategoriaNotFoundExceptions extends RuntimeException {

    public CategoriaNotFoundExceptions(String message) {
        super(message);
    }

}
