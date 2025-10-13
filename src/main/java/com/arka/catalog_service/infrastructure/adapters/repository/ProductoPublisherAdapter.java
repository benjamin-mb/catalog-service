package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoPublisherGateway;
import com.arka.catalog_service.infrastructure.DTO.ProductsRunningLowStock;
import com.arka.catalog_service.infrastructure.messages.PublisherProducto;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoPublisherAdapter implements ProductoPublisherGateway {

    private final PublisherProducto publisherProducto;

    public ProductoPublisherAdapter(PublisherProducto publisherProducto) {
        this.publisherProducto = publisherProducto;
    }

    @Override
    public void publisherStockRunningLow(Productos producto) {
        ProductsRunningLowStock dto=new ProductsRunningLowStock(
                producto.getId(),
                producto.getNombre(),
                producto.getStock(),
                producto.getProveedor()
        );
        publisherProducto.publisherStockRunningLow(dto);
    }
}
