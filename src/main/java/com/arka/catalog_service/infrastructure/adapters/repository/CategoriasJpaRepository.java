package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.infrastructure.adapters.entity.CategoriasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriasJpaRepository extends JpaRepository<CategoriasEntity,Integer> {
    Optional<CategoriasEntity> findById(Integer id);
    Optional<CategoriasEntity> findByNombre(String nombre);
    Optional<List<CategoriasEntity>>findAllByTipo(String tipo);
    List<CategoriasEntity>findAll();
    Boolean existsByNombre(String nombre);
    boolean existsById(Integer id);
}
