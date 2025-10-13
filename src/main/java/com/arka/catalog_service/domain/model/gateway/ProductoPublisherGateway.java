package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Productos;

import java.util.List;

public interface ProductoPublisherGateway {
    void publisherStockRunningLow(Productos producto);
}
