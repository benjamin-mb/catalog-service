package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Proveedor;
import com.arka.catalog_service.infrastructure.adapters.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvedooresJpaRepository extends JpaRepository<ProveedorEntity,Integer> {
    ProveedorEntity findById(Long id);
}
