package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;

public class UpdateDeleteProductoUseCase {

    private final ProductoGateway productoGateway;
    private final CategoriaGateway categoriaGateway;

    public UpdateDeleteProductoUseCase(ProductoGateway productoGateway, CategoriaGateway categoriaGateway) {
        this.productoGateway = productoGateway;
        this.categoriaGateway = categoriaGateway;
    }

    public Productos update (Productos producto){

        Productos productoExiste=productoGateway.findById(producto.getId())
                .orElseThrow(()->new IllegalArgumentException("No product has been found with id:"+producto.getId()));

        if (producto.getNombre() != null) {
            productoExiste.setNombre(producto.getNombre());
        }
        if (producto.getMarca() != null) {
            productoExiste.setMarca(producto.getMarca());
        }
        if (producto.getPrecio() != null) {
            if (producto.getPrecio() < 0) {
                throw new IllegalArgumentException("price can't be negative");
            }
            productoExiste.setPrecio(producto.getPrecio());
        }
        if (producto.getStock() != null) {
            if (producto.getStock() < 0) {
                throw new IllegalArgumentException("stock can't be negative");
            }
            productoExiste.setStock(producto.getStock());
        }
        if (producto.getCategoria() != null) {
            if (categoriaGateway.existsById(producto.getCategoria().getId())){
                productoExiste.setCategoria(producto.getCategoria());
            }
            else {
                throw new IllegalArgumentException("category id" + producto.getCategoria().getId() + "do not exist");
            }
        }

        return productoGateway.updateProduct(productoExiste);
    }

    public Productos delete(Integer id){

        Productos productoEliminar=productoGateway.findById(id)
                .orElseThrow(()->new IllegalArgumentException("No product has been found with id:"+id));

        productoGateway.deleteById(id);
        return productoEliminar;
    }

}
