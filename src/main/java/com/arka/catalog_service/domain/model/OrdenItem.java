package com.arka.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenItem {
    private Integer idProducto;
    private Integer cantidad;
}
