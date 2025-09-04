package com.arka.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Categorias {
    private Integer id;
    private String nombre;
    private String caracteristicas;
    private String tipo;
    private List<Productos> productos;

    public Categorias(String nombre, String caracteristicas, String tipo) {
        this.nombre = nombre;
        this.caracteristicas = caracteristicas;
        this.tipo = tipo;
        this.productos=new ArrayList<>();
    }
}
