package com.arka.catalog_service.infrastructure.adapters.repository;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.infrastructure.adapters.entity.ProductosEntity;
import com.arka.catalog_service.infrastructure.adapters.mappers.ProductoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductosRepositoryImplements implements ProductoGateway {

private final ProductosJpaRepository repository;
private final CategoriasJpaRepository categoriasRepository;
private final ProvedooresJpaRepository provedooresRepository;
private final ProductoMapper mapper;

    public ProductosRepositoryImplements(ProductosJpaRepository repository, CategoriasJpaRepository categoriasRepository, ProvedooresJpaRepository provedooresRepository, ProductoMapper mapper) {
        this.repository = repository;
        this.categoriasRepository = categoriasRepository;
        this.provedooresRepository = provedooresRepository;
        this.mapper = mapper;
    }


    public Productos create(Productos pruducto){
        ProductosEntity productosEntity=mapper.toEntity(pruducto);
        ProductosEntity productoSaved=repository.save(productosEntity);
        return mapper.toModel(productoSaved);
    }

    @Override
    public Optional<Productos> findById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<Productos> findByNombre(String nombre) {
        return repository.findByNombre(nombre).map(mapper::toModel);
    }

    @Override
    public List<Productos> findAllByMarca(String marca) {
        return repository.findAllByMarca(marca)
                .stream().map(mapper::toModel).toList();
    }

    @Override
    public List<Productos> findAllByCategoria(Long categoria) {
        return repository.findAllByCategoria(categoria)
                .stream().map(mapper::toModel)
                .toList();
    }

    @Override
    public List<Productos> findAll() {
        return repository.findAll()
                .stream().map(mapper::toModel)
                .toList();
    }

    @Override
    public Boolean existsByNombre(String nombre) {
        return repository.existsByNombre(nombre);
    }

    @Override
    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Boolean existsByNombreAndCategoria(String nombre, Long categoria) {
        return repository.existsByNombreAndCategoria(nombre,categoria);
    }

    @Override
    public Productos updateProduct(Productos producto) {
        if (repository.existsById(producto.getId())){
            throw new IllegalArgumentException("product not found to be updated");
        }
        ProductosEntity productosEntity=mapper.toEntity(producto);
        ProductosEntity productoUpdated=repository.save(productosEntity);
        return mapper.toModel(productoUpdated);
    }

    @Override
    public Productos deleteById(Long id) {
        ProductosEntity producto=repository.findById(id)
                        .orElseThrow(()->new  IllegalArgumentException("product not found to be deleted"));
        repository.deleteById(id);
        return mapper.toModel(producto);
    }
}
