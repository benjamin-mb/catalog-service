package com.arka.catalog_service.infrastructure.adapters.mappers;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.infrastructure.adapters.entity.CategoriasEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class CategoriaMapper {

    public Categorias toDomain(CategoriasEntity categoriaEntity){
        if (categoriaEntity == null)return null;
        Categorias categoria=new Categorias();
        categoria.setId(categoriaEntity.getId());
        categoria.setNombre(categoriaEntity.getNombre());
        categoria.setCaracteristicas(categoriaEntity.getCaracteristicas());
        categoria.setTipo(categoriaEntity.getTipo());
        return categoria;
    }

    public  CategoriasEntity toEntity(Categorias categoria){
        if (categoria == null)return null;
        CategoriasEntity categoriaEntity=new CategoriasEntity();
        categoriaEntity.setNombre(categoria.getNombre());
        categoriaEntity.setCaracteristicas(categoria.getCaracteristicas());
        categoriaEntity.setTipo(categoria.getTipo());
        categoriaEntity.setId(categoria.getId());
        return categoriaEntity;
    }
}
