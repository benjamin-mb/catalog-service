package com.arka.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Productos {
    private Long id;
    private String nombre;
    private Integer precio;
    private Integer stock;
    private String caracteristicas;
    private String marca;
    private Long categoria;
    private Long proveedor;

    public Productos(String nombre,Integer precio, Integer stock, String caracteristicas, String marca, Long categoria, Long proveedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.caracteristicas = caracteristicas;
        this.marca = marca;
        this.categoria = categoria;
        this.proveedor = proveedor;
    }
}
