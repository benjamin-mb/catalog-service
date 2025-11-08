package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class GetterProductUseCase {

    private final ProductoGateway productoGateway;

    public GetterProductUseCase(ProductoGateway productoGateway) {
        this.productoGateway = productoGateway;
    }

    public Productos getById(Integer id){
        return productoGateway.findById(id)
                .orElseThrow(()->new EntityNotFoundException("product with id:"+id+" was not found"));

    }

    public Productos getByNombre(String nombre){
        String nombreTrim=nombre.trim();
        return productoGateway.findByNombre(nombreTrim)
                .orElseThrow(()->new EntityNotFoundException("producto no encontrado con nombre: "+nombre));

    }

    public List<Productos> getByMarca(String marca){
        String marcaTrim=marca.trim();
        List<Productos> productoList=productoGateway.findAllByMarca(marcaTrim);
        if (productoList.isEmpty()){
            throw new EntityNotFoundException("there are no products with that brand");
        }
        return  productoList;
    }

    public List<Productos> getByCategoria(Integer categoria){
        List<Productos> productoList=productoGateway.findAllByCategoria(categoria);
        if (productoList.isEmpty()){
            throw new EntityNotFoundException("there are no products with that category");
        }
        return  productoList;
    }

    public List<Productos>getAll(){
        List<Productos>productosList=productoGateway.findAll();
        return productosList;
    }
}
