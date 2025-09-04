package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.infrastructure.adapters.entity.ProductosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductosJpaRepository extends JpaRepository<ProductosEntity,Integer> {

    Optional<ProductosEntity> findById(Integer id);
    Optional<ProductosEntity> findByNombre(String nombre);
    List<ProductosEntity> findAllByMarca(String marca);
    List<ProductosEntity> findAllByCategoria_id(Integer categoria);
    List<ProductosEntity>findAll();
    boolean existsByNombre(String nombre);
    boolean existsById(Integer id);
    boolean existsByNombreAndCategoria_Id(String nombre, Integer categoria);

}
