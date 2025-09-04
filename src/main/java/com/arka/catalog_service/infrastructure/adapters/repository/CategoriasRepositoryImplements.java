package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.infrastructure.adapters.entity.CategoriasEntity;
import com.arka.catalog_service.infrastructure.adapters.entity.ProductosEntity;
import com.arka.catalog_service.infrastructure.adapters.mappers.CategoriaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriasRepositoryImplements implements CategoriaGateway {

    private final CategoriasJpaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriasRepositoryImplements(CategoriasJpaRepository repository, CategoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Categorias create(Categorias categoria) {
        CategoriasEntity categoriaEntity=mapper.toEntity(categoria);
        CategoriasEntity categoriaSaved=repository.save(categoriaEntity);
        return mapper.toDomain(categoriaSaved);
    }

    @Override
    public Optional<Categorias> findById(Integer id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Categorias> findByNombre(String nombre) {
        return repository.findByNombre(nombre).map(mapper::toDomain);
    }

    @Override
    public List<Categorias> findAllByTipo(String tipo) {
        return repository.findAllByTipo(tipo).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public List<Categorias> findAll() {
        return repository.findAll().stream().
                map(mapper::toDomain)
                .toList();
    }

    @Override
    public Boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public Categorias updateCategoria(Categorias categoria) {
        if (!repository.existsById(categoria.getId())){
            throw new IllegalArgumentException("category not found with id:"+categoria.getId());
        }
        CategoriasEntity categorisEntity=mapper.toEntity(categoria);
        CategoriasEntity categoriaUpdated=repository.save(categorisEntity);
        return mapper.toDomain(categoriaUpdated);
    }

    @Override
    public Categorias deleteById(Integer id) {
        CategoriasEntity categorias=repository.findById(id)
                .orElseThrow(()->new  IllegalArgumentException("category not found to be deleted"));
        repository.deleteById(categorias.getId());
        return mapper.toDomain(categorias);
    }
}
