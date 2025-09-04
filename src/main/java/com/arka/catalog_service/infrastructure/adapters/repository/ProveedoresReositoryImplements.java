package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Proveedor;
import com.arka.catalog_service.domain.model.gateway.ProvedorGateway;
import com.arka.catalog_service.infrastructure.adapters.entity.ProductosEntity;
import com.arka.catalog_service.infrastructure.adapters.entity.ProveedorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProveedoresReositoryImplements implements ProvedorGateway {

    private final ProvedooresJpaRepository repository;

    public ProveedoresReositoryImplements(ProvedooresJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean existsById(Integer id) {
        return repository.existsById(id);
    }
}
