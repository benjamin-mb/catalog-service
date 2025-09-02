package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostProductUseCase {

    private final CategoriaGateway categoriaGateway;
    private final ProductoGateway productoGateway;

    public PostProductUseCase(CategoriaGateway categoriaGateway, ProductoGateway productoGateway) {
        this.categoriaGateway = categoriaGateway;
        this.productoGateway = productoGateway;
    }

    public Productos create(Productos producto){

        if (categoriaGateway.existsById(producto.getId())){
            throw new IllegalArgumentException(producto.getId()+"is already register on another product");
        }

        Categorias categoria=categoriaGateway.findById(producto.getCategoria())
                .orElseThrow(()->new IllegalArgumentException("la categoria"+ producto.getCategoria()));

        if(productoGateway.existsByNombreAndCategoria(producto.getNombre(),producto.getCategoria())){
            throw new IllegalArgumentException("the user already exists on the category"+producto.getCategoria().getId());

        }

        if (producto.getStock()<0){
            throw new IllegalArgumentException("the stock can't be below 0");
        }

        if (productoGateway.existsByNombre(producto.getNombre())){
            throw new IllegalArgumentException("the name is already registered");
        }

        if (producto.getPrecio()<0){
            throw new IllegalArgumentException("the product has to have a price");
        }

         return productoGateway.create(producto);

    }
}
