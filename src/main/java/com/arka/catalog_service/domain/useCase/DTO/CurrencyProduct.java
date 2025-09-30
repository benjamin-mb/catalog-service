package com.arka.catalog_service.domain.useCase.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyProduct {
    private Integer id;
    private String nombre;
    private Integer precio;
    private Integer stock;
    private String caracteristicas;
    private String marca;
    private Integer categoria;
    private Integer proveedor;

    public CurrencyProduct(String nombre,Integer precio, Integer stock, String caracteristicas, String marca, Integer categoria, Integer proveedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.caracteristicas = caracteristicas;
        this.marca = marca;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }
}
