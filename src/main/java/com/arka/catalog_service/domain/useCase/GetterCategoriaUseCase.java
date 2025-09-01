package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.GlobalExceptions.CategoriaNotFoundExceptions;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;

import java.util.List;

public class GetterCategoriaUseCase {

    private final CategoriaGateway categoriaGateway;

    public GetterCategoriaUseCase(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public Categorias getById(Integer id){

        Categorias categoria=categoriaGateway.findById(id)
                .orElseThrow((()->new IllegalArgumentException("no category has been found with id "+id)));

        return categoria;
    }

    public Categorias getByName(String name){
      String nombreLimpio= name.trim();

      Categorias categoria=categoriaGateway.findByNombre(nombreLimpio)
              .orElseThrow(()->new CategoriaNotFoundExceptions("no category has been found with name:" +nombreLimpio));
      return categoria;
    }

    public List<Categorias> getAllByTipo(String tipo){
        String tipoLimpio= tipo.trim();

        List<Categorias> categorias=categoriaGateway.findAllByTipo(tipoLimpio)
                .orElseThrow(()->new CategoriaNotFoundExceptions("there are no categories under that type"));
        return categorias;
    }

    public List<Categorias> getAll(){
        List<Categorias> categorias=categoriaGateway.findAll();
        return categorias;
    }

}
