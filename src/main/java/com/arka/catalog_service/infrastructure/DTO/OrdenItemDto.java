package com.arka.catalog_service.infrastructure.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenItemDto {
    private Integer idProducto;
    private Integer cantidad;
}
