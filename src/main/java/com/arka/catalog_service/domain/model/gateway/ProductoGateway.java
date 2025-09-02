package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Productos;

import java.util.List;
import java.util.Optional;

public interface ProductoGateway {

    Productos create(Productos producto);
    Optional<Productos> findById(Long id);
    Optional<Productos> findByNombre(String nombre);
    List<Productos> findAllByMarca(String marca);
    List<Productos> findAllByCategoria(Long categoria);
    List<Productos>findAll();
    Boolean existsByNombre(String nombre);
    Boolean existsById(Long id);
    Boolean existsByNombreAndCategoria(String nombre, Long categoria);
    Productos updateProduct(Productos producto);
    Productos deleteById(Long id);


}
