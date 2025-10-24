package com.arka.catalog_service.domain.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCreateDto {
    private String nombre;
    private Integer precio;
    private Integer stock;
    private String caracteristicas;
    private String marca;
    private Integer categoria;
    private Integer proveedor;
}
