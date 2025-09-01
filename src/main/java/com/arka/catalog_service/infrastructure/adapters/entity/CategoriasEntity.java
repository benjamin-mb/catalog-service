package com.arka.catalog_service.infrastructure.adapters.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categorias")
@Data
public class CategoriasEntity {
    @Column(name = "id_categoria")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, unique = true,length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(length = 50)
    private String tipo;

    @OneToMany(mappedBy = "categoria",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductosEntity> productos;

    public CategoriasEntity(String nombre, String caracteristicas, String tipo) {
        this.nombre = nombre;
        this.caracteristicas = caracteristicas;
        this.tipo = tipo;
    }
}
