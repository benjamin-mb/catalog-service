package com.arka.catalog_service.domain.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaCreateDto {
    private String nombre;
    private String caracteristicas;
    private String tipo;
}
