package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.infrastructure.messages.DTO.ProductsRunningLowStock;
import com.arka.catalog_service.infrastructure.messages.PublisherProducto;

public class ReduceStockProductUseCase {

    private final ProductoGateway productoGateway;
    private final PublisherProducto publisherProducto;

    public ReduceStockProductUseCase(ProductoGateway productoGateway, PublisherProducto publisherProducto) {
        this.productoGateway = productoGateway;
        this.publisherProducto = publisherProducto;
    }

    public Productos reduceStock( Integer productoId,Integer cantidad){

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
            ProductsRunningLowStock event = new ProductsRunningLowStock(
                    productoAfterSell.getId(),
                    productoAfterSell.getNombre(),
                    productoAfterSell.getStock(),
                    productoAfterSell.getProveedor()
            );
            publisherProducto.publisherStockRunningLow(event);

        }
        return  productoAfterSell;
    }

}
