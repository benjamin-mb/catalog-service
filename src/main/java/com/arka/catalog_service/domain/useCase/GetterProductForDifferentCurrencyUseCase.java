package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.useCase.DTO.CurrencyProduct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetterProductForDifferentCurrencyUseCase {

    private final ProductoGateway productoGateway;

    public GetterProductForDifferentCurrencyUseCase(ProductoGateway productoGateway) {
        this.productoGateway = productoGateway;
    }

    public CurrencyProduct getById(Integer id, Double currency){
        Optional<Productos> producto =productoGateway.findById(id);
        CurrencyProduct currencyProduct = new CurrencyProduct();
        if (producto.isEmpty()){
           throw new NotFoundException("producto no encontrado con id: "+ id);
        }
        if (producto.isPresent()) {
            currencyProduct.setId(producto.get().getId());
            currencyProduct.setNombre(producto.get().getNombre());
            Double calculation= producto.get().getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            currencyProduct.setStock(producto.get().getStock());
            currencyProduct.setCaracteristicas(producto.get().getCaracteristicas());
            currencyProduct.setMarca(producto.get().getMarca());
            currencyProduct.setCategoria(producto.get().getCategoria());
            currencyProduct.setProveedor(producto.get().getProveedor());
        }
        return currencyProduct;
    }

    public CurrencyProduct getByNombre(String nombre,Double currency){
        Optional<Productos> producto =productoGateway.findByNombre(nombre);
        CurrencyProduct currencyProduct = new CurrencyProduct();
        if (producto.isEmpty()){
            throw new NotFoundException("producto no encontrado con nombre: "+nombre);
        }
        if (producto.isPresent()) {
            currencyProduct.setId(producto.get().getId());
            currencyProduct.setNombre(producto.get().getNombre());
            Double calculation= producto.get().getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            currencyProduct.setStock(producto.get().getStock());
            currencyProduct.setCaracteristicas(producto.get().getCaracteristicas());
            currencyProduct.setMarca(producto.get().getMarca());
            currencyProduct.setCategoria(producto.get().getCategoria());
            currencyProduct.setProveedor(producto.get().getProveedor());
        }
        return currencyProduct;
    }

    public List<CurrencyProduct> getByMarca(String marca,Double currency){
        List<Productos> productoList=productoGateway.findAllByMarca(marca);
        List<CurrencyProduct>newList=new ArrayList<>();
        if (productoList.isEmpty()){
            return new ArrayList<>();
        }
        for (Productos producto : productoList) {
            CurrencyProduct currencyProduct = new CurrencyProduct();
            currencyProduct.setId(producto.getId());
            currencyProduct.setNombre(producto.getNombre());
            Double calculation= producto.getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            currencyProduct.setStock(producto.getStock());
            currencyProduct.setCaracteristicas(producto.getCaracteristicas());
            currencyProduct.setMarca(producto.getMarca());
            currencyProduct.setCategoria(producto.getCategoria());
            currencyProduct.setProveedor(producto.getProveedor());
            newList.add(currencyProduct);
        }
        return newList;

    }

    public List<CurrencyProduct> getByCategoria(Integer categoria,Double currency){
        List<Productos> productoList=productoGateway.findAllByCategoria(categoria);
        List<CurrencyProduct>newList=new ArrayList<>();
        if (productoList.isEmpty()){
            return new ArrayList<>();
        }
        for (Productos producto : productoList) {
            CurrencyProduct currencyProduct = new CurrencyProduct();
            currencyProduct.setId(producto.getId());
            currencyProduct.setNombre(producto.getNombre());
            Double calculation= producto.getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            currencyProduct.setStock(producto.getStock());
            currencyProduct.setCaracteristicas(producto.getCaracteristicas());
            currencyProduct.setMarca(producto.getMarca());
            currencyProduct.setCategoria(producto.getCategoria());
            currencyProduct.setProveedor(producto.getProveedor());
            newList.add(currencyProduct);
        }
        return newList;
    }

    public List<CurrencyProduct>getAll(Double currency){
        List<Productos>productosList=productoGateway.findAll();
        List<CurrencyProduct>newList=new ArrayList<>();
        for (Productos producto : productosList) {
            CurrencyProduct currencyProduct = new CurrencyProduct();
            currencyProduct.setId(producto.getId());
            currencyProduct.setNombre(producto.getNombre());
            Double calculation= producto.getPrecio() * currency;
            Integer priceCurrency=(int) Math.round(calculation);
            currencyProduct.setPrecio(priceCurrency);
            currencyProduct.setStock(producto.getStock());
            currencyProduct.setCaracteristicas(producto.getCaracteristicas());
            currencyProduct.setMarca(producto.getMarca());
            currencyProduct.setCategoria(producto.getCategoria());
            currencyProduct.setProveedor(producto.getProveedor());
            newList.add(currencyProduct);
        }
        return newList;

    }
}