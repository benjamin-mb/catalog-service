package com.arka.catalog_service.infrastructure.adapters.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "usuarios")
@Data
@NoArgsConstructor
@Entity
public class ProductosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_producto")
    private Long id;

    @Column(nullable = false, unique = true,length = 70)
    private String nombre;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String caracteristicas;

    @Column(nullable = false)
    private String marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor",referencedColumnName = "id_proveedor")
    private ProveedorEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category",referencedColumnName = "id_category")
    private CategoriasEntity categoria;

    public ProductosEntity(String nombre, Integer precio, Integer stock, String caracteristicas, String marca, ProveedorEntity proveedor, CategoriasEntity categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.caracteristicas = caracteristicas;
        this.marca = marca;
        this.proveedor = proveedor;
        this.categoria = categoria;
    }
}
