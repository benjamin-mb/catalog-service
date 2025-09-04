package com.arka.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Proveedor {
    private Integer id;
    private String nombre;
    private String telefono;
    private String caracteristicas;

    public Proveedor(String nombre, String telefono, String caracteristicas) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.caracteristicas = caracteristicas;
    }
}
