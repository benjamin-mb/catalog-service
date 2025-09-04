package com.arka.catalog_service.domain.model.gateway;

import com.arka.catalog_service.domain.model.Productos;

import java.util.List;
import java.util.Optional;

public interface ProductoGateway {

    Productos save(Productos producto);
    Optional<Productos> findById(Integer id);
    Optional<Productos> findByNombre(String nombre);
    List<Productos> findAllByMarca(String marca);
    List<Productos> findAllByCategoria(Integer categoria);
    List<Productos>findAll();
    Boolean existsByNombre(String nombre);
    Boolean existsById(Integer id);
    Boolean existsByNombreAndCategoria(String nombre, Integer categoria);
    Productos updateProduct(Productos producto);
    Productos deleteById(Integer id);


}
