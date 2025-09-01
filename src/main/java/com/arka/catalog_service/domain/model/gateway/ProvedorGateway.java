package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Proveedor;

import java.util.List;

public interface ProvedorGateway {
    Proveedor findById(Long id);
}
