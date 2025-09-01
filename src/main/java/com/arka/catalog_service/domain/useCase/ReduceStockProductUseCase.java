package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;

public class ReduceStockProductUseCase {

    private final ProductoGateway productoGateway;

    public ReduceStockProductUseCase(ProductoGateway productoGateway) {
        this.productoGateway = productoGateway;
    }

    public Productos reduceStock(Integer productoId,Integer cantidad){

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reducir debe ser mayor que 0");
        }

        Productos producto=productoGateway.findById(productoId)
                .orElseThrow(()->new IllegalArgumentException("product not found with id"+productoId));

        if (producto.getStock()==null||producto.getStock()<cantidad){
            throw new IllegalArgumentException("There is no stock available on this product");
        }

        producto.setStock(producto.getStock()-cantidad);
        Productos productoAfterSell=productoGateway.updateProduct(producto);

        if(productoAfterSell.getStock()<5){
            System.out.println("product "+productoAfterSell.getId()+"running out of stock");
        }
        return  productoAfterSell;
    }

}
