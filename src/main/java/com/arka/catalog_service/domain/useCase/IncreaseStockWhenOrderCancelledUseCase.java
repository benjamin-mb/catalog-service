package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.GlobalExceptions.ProductException;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;

public class IncreaseStockWhenOrderCancelledUseCase {

    private final ProductoGateway productoGateway;

    public IncreaseStockWhenOrderCancelledUseCase(ProductoGateway productoGateway) {
        this.productoGateway = productoGateway;
    }

    public void increaseStock(Integer idProducto, Integer cantidad) {

        Productos producto = productoGateway.findById(idProducto)
                .orElseThrow(() -> new ProductException("Product not found with id: " + idProducto));

        Integer nuevoStock = producto.getStock() + cantidad;
        producto.setStock(nuevoStock);


        productoGateway.updateProduct(producto);

    }
}
