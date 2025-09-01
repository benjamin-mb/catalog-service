package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Proveedor;

import java.util.List;

public interface ProvedorGateway {
    Proveedor create(Proveedor proveedor);
    Proveedor findById(Long id);
    List<Proveedor> findAll();
    void deleteById(Long id);
    Proveedor updateProovedor(Proveedor proveedor);
    Boolean existsByNombre(String nombre);
    Boolean existsById(Integer id);

}
