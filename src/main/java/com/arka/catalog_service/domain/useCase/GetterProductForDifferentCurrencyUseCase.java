package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.useCase.DTO.CurrencyProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

public class GetterProductForDifferentCurrencyUseCase {

    private final ProductoGateway productoGateway;

    public GetterProductForDifferentCurrencyUseCase(ProductoGateway productoGateway) {
        this.productoGateway = productoGateway;
    }

    public CurrencyProduct getById(Integer id, Double currency){
        Optional<Productos> producto =productoGateway.findById(id);
        if (producto.isEmpty()){
           throw new NotFoundException("producto no encontrado con id: "+ id);
        }
        if (producto.isPresent()) {
            CurrencyProduct currencyProduct = new CurrencyProduct();
            currencyProduct.setId(producto.get().getId());
            currencyProduct.setNombre(producto.get().getNombre());
            Double calculation= producto.get().getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            cu
        }
    }

    public Productos getByNombre(String nombre){
        return productoGateway.findByNombre(nombre)
                .orElseThrow(()->new IllegalArgumentException("producto no encontrado con nombre: "+nombre));

    }

    public List<Productos> getByMarca(String marca){
        List<Productos> productoList=productoGateway.findAllByMarca(marca);
        if (productoList.isEmpty()){
            throw new IllegalArgumentException("there are no products with that brand");
        }
        return  productoList;
    }

    public List<Productos> getByCategoria(Integer categoria){
        List<Productos> productoList=productoGateway.findAllByCategoria(categoria);
        if (productoList.isEmpty()){
            throw new IllegalArgumentException("there are no products with that brand");
        }
        return  productoList;
    }

    public List<Productos>getAll(){
        List<Productos>productosList=productoGateway.findAll();
        return productosList;
    }
}
