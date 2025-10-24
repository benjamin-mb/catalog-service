package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.CategoriaCreateDto;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;

import java.util.HashSet;

public class PostCategoriaUseCase {

    private final CategoriaGateway categoriaGateway;


    public PostCategoriaUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public Categorias create(CategoriaCreateDto categoria){
        if (categoria.getNombre()==null || categoria.getNombre().isBlank()){
            throw new IllegalArgumentException("nombre is obligatory");
        }

        if (categoria.getCaracteristicas()==null || categoria.getCaracteristicas().isBlank()){
            throw new IllegalArgumentException("caracteristicas is obligatory");
        }

        if (categoria.getTipo()==null || categoria.getTipo().isBlank()){
            throw new IllegalArgumentException("tipo is obligatory");
        }

        if (categoriaGateway.existsByNombre(categoria.getNombre())){
            throw new IllegalArgumentException("there is already a category set with name "+categoria.getNombre());
        }

        categoria.setNombre(categoria.getNombre().trim());
        categoria.setCaracteristicas(categoria.getCaracteristicas().trim());
        categoria.setTipo(categoria.getTipo().trim());

        Categorias categoriaEntity=new Categorias(
                categoria.getNombre(),
                categoria.getCaracteristicas(),
                categoria.getTipo()
        );
        return categoriaGateway.create(categoriaEntity);
    }
}
