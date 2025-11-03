package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.ProductoCreateDto;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.model.gateway.ProvedorGateway;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostProductUseCase {

    private final CategoriaGateway categoriaGateway;
    private final ProductoGateway productoGateway;
    private final ProvedorGateway proveedorGateway;

    public PostProductUseCase(CategoriaGateway categoriaGateway, ProductoGateway productoGateway, ProvedorGateway proveedorGateway) {
        this.categoriaGateway = categoriaGateway;
        this.productoGateway = productoGateway;
        this.proveedorGateway = proveedorGateway;
    }

    public Productos create(ProductoCreateDto producto){

        Categorias categoria=categoriaGateway.findById(producto.getCategoria())
                .orElseThrow(()->new IllegalArgumentException("la categoria"+ producto.getCategoria()));

        if(productoGateway.existsByNombreAndCategoria(producto.getNombre(),producto.getCategoria())){
            throw new IllegalArgumentException("the product already exists on the category"+producto.getCategoria());

        }

        if (producto.getStock()<=5){
            throw new IllegalArgumentException("the stock can't be below 0");
        }

        if (productoGateway.existsByNombre(producto.getNombre())){
            throw new IllegalArgumentException("the name is already registered");
        }

        if (producto.getPrecio()<0){
            throw new IllegalArgumentException("the product has to have a price");
        }

        if (producto.getCaracteristicas().isBlank() || producto.getCaracteristicas()==null){
            throw new IllegalArgumentException("caracteristics field can not be blank");
        }

        if (producto.getMarca().isBlank() || producto.getMarca()==null){
            throw new IllegalArgumentException("marca field can not be blank");
        }


        if (!proveedorGateway.existsById(producto.getProveedor())){
            throw new IllegalArgumentException("proveedor with id"+ producto.getProveedor()+" has not been found");
        }
        Productos productoEntity=new Productos(
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCaracteristicas(),
                producto.getMarca(),
                producto.getCategoria(),
                producto.getProveedor()
        );

        return productoGateway.save(productoEntity);
    }

}
