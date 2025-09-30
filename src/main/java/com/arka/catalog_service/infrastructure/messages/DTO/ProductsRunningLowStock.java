package com.arka.catalog_service.infrastructure.messages.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsRunningLowStock {
    private Integer producto_id;
    private String nombre_producto;
    private Integer stock_Actual;
    private Integer proveedor_id;
}
