package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.CategoriaCreateDto;
import com.arka.catalog_service.domain.model.GlobalExceptions.CategoriaNotFoundExceptions;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;

public class UpdateDeleteCategoriaUseCase {

    private final CategoriaGateway categoriaGateway;

    public UpdateDeleteCategoriaUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public Categorias update(CategoriaCreateDto categoria, Integer idCategoria){
        Categorias categoriaUpdated=categoriaGateway.findById(idCategoria)
                .orElseThrow(()->new CategoriaNotFoundExceptions("category not found by id"+idCategoria));

        if (categoria.getNombre() != null){
            String nuevoNombre = categoria.getNombre().trim();
            if (!nuevoNombre.equalsIgnoreCase(categoriaUpdated.getNombre()) &&
                    categoriaGateway.existsByNombre(nuevoNombre)){
                throw new IllegalArgumentException("there is already another category with that name");
            }
            categoriaUpdated.setNombre(nuevoNombre);
        }

        if (categoria.getTipo()!= null){
            String tipoLimpio = categoria.getTipo().trim();
            if(tipoLimpio.isEmpty()){
                throw new IllegalArgumentException("type can not be blank");
            }
            categoriaUpdated.setTipo(tipoLimpio);
        }

        if (categoria.getCaracteristicas()!=null){
            String caracLimpias = categoria.getCaracteristicas().trim();
            if(caracLimpias.isEmpty()){
                throw new IllegalArgumentException("characteristics can not be blank");
            }
            categoriaUpdated.setCaracteristicas(caracLimpias);
        }


        return categoriaGateway.updateCategoria(categoriaUpdated);
    }

    public Categorias deleteById(Integer id){
        Categorias categoriaToDelete=categoriaGateway.findById(id)
                .orElseThrow(()->new CategoriaNotFoundExceptions("category has not been found with id:"+id));

        return categoriaGateway.deleteById(id);
    }

}
