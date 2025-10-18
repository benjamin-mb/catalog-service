package com.arka.catalog_service.domain.useCase;

import com.arka.catalog_service.domain.model.OrdenItem;

import java.util.List;

public class WrapperIncreaseStockUseCase {

    private final IncreaseStockWhenOrderCancelledUseCase increaseStockWhenOrderCancelledUseCase;

    public WrapperIncreaseStockUseCase(IncreaseStockWhenOrderCancelledUseCase increaseStockWhenOrderCancelledUseCase) {
        this.increaseStockWhenOrderCancelledUseCase = increaseStockWhenOrderCancelledUseCase;
    }

    public void wrapper(List<OrdenItem>items){

        items.forEach(item -> {
            try{
                increaseStockWhenOrderCancelledUseCase.increaseStock(item.getIdProducto(),item.getCantidad());
            } catch (Exception e) {

            }
        });
    }
}
