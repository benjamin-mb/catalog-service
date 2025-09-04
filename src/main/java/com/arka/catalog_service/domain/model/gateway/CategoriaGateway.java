package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.Productos;

import java.util.List;
import java.util.Optional;

public interface CategoriaGateway {
    Categorias create(Categorias categoria);
    Optional<Categorias> findById(Integer id);
    Optional<Categorias> findByNombre(String nombre);
    List<Categorias>findAllByTipo(String tipo);
    List<Categorias>findAll();
    Boolean existsByNombre(String nombre);
    Boolean existsById(Integer id);
    Categorias updateCategoria(Categorias categoria);
    Categorias deleteById(Integer id);
}
