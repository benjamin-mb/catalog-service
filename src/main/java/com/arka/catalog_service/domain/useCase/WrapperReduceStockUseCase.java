package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.OrdenItem;

import java.util.List;

public class WrapperReduceStockUseCase {

    private final ReduceStockProductUseCase reduceStockProductUseCase;

    public WrapperReduceStockUseCase(ReduceStockProductUseCase reduceStockProductUseCase) {
        this.reduceStockProductUseCase = reduceStockProductUseCase;
    }

    public void wrapper(List<OrdenItem> detalles){
        detalles.forEach(detalle->{
            reduceStockProductUseCase.reduceStock(detalle.getIdProducto(),detalle.getCantidad());
        });
    }
}
