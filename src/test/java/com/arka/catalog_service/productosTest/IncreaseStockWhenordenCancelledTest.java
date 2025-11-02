package com.arka.catalog_service.productosTest;

import com.arka.catalog_service.domain.model.GlobalExceptions.ProductNotFoundException;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.useCase.IncreaseStockWhenOrderCancelledUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("test para increase stock cuando una orden es cancelada")
public class IncreaseStockWhenordenCancelledTest {

    @Mock
    private ProductoGateway productoGateway;

    @InjectMocks
    private IncreaseStockWhenOrderCancelledUseCase increaseStockWhenOrderCancelledUseCase;

    private Productos productoTest;

    @BeforeEach
    void setUp() {
        productoTest = new Productos(
                1,
                "Laptop Dell",
                2000,
                5,
                "Core i7, 16GB RAM",
                "Dell",
                1,
                1
        );
    }

//    ==============================================
//            CASOS DE EXITO
//    ==============================================

    @Test
    @DisplayName("LN01 - incrementar el stcok de un producto exitosamente")
    void increaseStockExitosa(){
        Integer cantidad=10;
        Integer nuevaCantidad=15;

        when(productoGateway.findById(productoTest.getId())).thenReturn(Optional.of(productoTest));

        Productos productoActualizado = new Productos(
                productoTest.getId(),
                productoTest.getNombre(),
                productoTest.getPrecio(),
                nuevaCantidad,
                productoTest.getCaracteristicas(),
                productoTest.getMarca(),
                productoTest.getCategoria(),
                productoTest.getProveedor()
        );

        when(productoGateway.updateProduct(any(Productos.class))).thenReturn(productoActualizado);

       increaseStockWhenOrderCancelledUseCase.increaseStock(productoActualizado.getId(),cantidad);

        verify(productoGateway, times(1)).findById(productoTest.getId());
        verify(productoGateway, times(1)).updateProduct(argThat(producto ->
                producto.getStock().equals(nuevaCantidad) &&
                        producto.getId().equals(productoActualizado.getId())
        ));
    }

//    =====================================================
//            CASOS DE ERROR
//    =====================================================

    @Test
    @DisplayName("LN02 - caso de error cuando el id del producto no es encontrado")
    void increaseStocKErrorIdNoEncontrado(){

        Integer id=89;
        Integer cantidad=6;

        when(productoGateway.findById(id)).thenReturn(Optional.empty());

        ProductNotFoundException exception= assertThrows(
                ProductNotFoundException.class,
                ()->increaseStockWhenOrderCancelledUseCase.increaseStock(id,cantidad)
        );

        assertEquals("Product not found with id: " + id, exception.getMessage());
        verify(productoGateway,times(1)).findById(any());
        verify(productoGateway,never()).updateProduct(any());

    }
}
