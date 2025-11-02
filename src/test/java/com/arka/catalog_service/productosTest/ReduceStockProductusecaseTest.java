package com.arka.catalog_service.productosTest;

import com.arka.catalog_service.domain.model.GlobalExceptions.ProductNotFoundException;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoPublisherGateway;
import com.arka.catalog_service.domain.useCase.ReduceStockProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para reducestock use case")
public class ReduceStockProductusecaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @Mock
    ProductoPublisherGateway productoPublisherGateway;

    @InjectMocks
    private ReduceStockProductUseCase reduceStockProductUseCase;

    private Productos productoTest;

    @BeforeEach
    void setUp() {
        productoTest = new Productos(
                1,
                "Laptop HP",
                1500,
                10,
                "Core i5, 8GB RAM",
                "HP",
                1,
                1
        );
    }

//    ============================================================
//                        CASOS DE EXITO
//    ============================================================
    @Test
    @DisplayName("LN01 - reducir stock correctamente cuando hay stock")
    void productoConStockSuficienteExitoso(){

        Integer cantidad=3;
        Integer stockEsperado=7;

        when(productoGateway.findById(productoTest.getId())).thenReturn(Optional.of(productoTest));

        Productos productoActualizado = new Productos(
                productoTest.getId(),
                productoTest.getNombre(),
                productoTest.getPrecio(),
                stockEsperado,
                productoTest.getCaracteristicas(),
                productoTest.getMarca(),
                productoTest.getCategoria(),
                productoTest.getProveedor()
        );

        when(productoGateway.updateProduct(any(Productos.class))).thenReturn(productoActualizado);

        Productos resultado=reduceStockProductUseCase.reduceStock(productoTest.getId(),cantidad);

        assertEquals(stockEsperado,productoActualizado.getStock());
        verify(productoGateway,times(1)).updateProduct(any(Productos.class));
        verify(productoGateway,times(1)).findById(productoTest.getId());
    }

    @Test
    @DisplayName("LN02 - producto actualizado correctamente con publisher enviado")
    void ReduceStockProcesadoExitosamenteYPublisherHecho(){

        Integer cantidad=8;
        Integer stockEsperado=2;

        when(productoGateway.findById(productoTest.getId())).thenReturn(Optional.of(productoTest));

        Productos productoActualizado = new Productos(
                productoTest.getId(),
                productoTest.getNombre(),
                productoTest.getPrecio(),
                stockEsperado,
                productoTest.getCaracteristicas(),
                productoTest.getMarca(),
                productoTest.getCategoria(),
                productoTest.getProveedor()
        );

        when(productoGateway.updateProduct(any(Productos.class))).thenReturn(productoActualizado);

        Productos resultado=reduceStockProductUseCase.reduceStock(productoTest.getId(),cantidad);

        assertEquals(stockEsperado,productoActualizado.getStock());
        verify(productoPublisherGateway,times(1)).publisherStockRunningLow(any(Productos.class));
    }

//    ==================================================
//                CASOS DE ERROR
//    ==================================================

    @Test
    @DisplayName("LN03 - lanzar excpecion cuando el producto no existe")
    void reduceStockErrorProductoInexistente(){
        Integer idInexistente=24;
        Integer cantidad=9;

        when(productoGateway.findById(idInexistente)).thenReturn(Optional.empty());

        ProductNotFoundException exception=assertThrows(
                ProductNotFoundException.class,
                ()->reduceStockProductUseCase.reduceStock(idInexistente,cantidad)
        );

        assertEquals("product not found with id"+idInexistente,exception.getMessage());
        verify(productoGateway,never()).updateProduct(any(Productos.class));
    }

    @Test
    @DisplayName("LN04 - Debe lanzar excepción cuando la cantidad es 0")
    void reduceStickErrorCantidadCero() {

        Integer cantidadCero = 0;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reduceStockProductUseCase.reduceStock(productoTest.getId(), cantidadCero)
        );

        assertTrue(exception.getMessage().contains("cantidad a reducir debe ser mayor que 0"));
        verify(productoGateway, never()).findById(anyInt());
    }

    @Test
    @DisplayName("LN05 - Debe lanzar excepción cuando la cantidad es negativa")
    void reduceStickErrorCantidadNegativa() {

        Integer cantidadNegativa = -5;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reduceStockProductUseCase.reduceStock(productoTest.getId(), cantidadNegativa)
        );

        assertTrue(exception.getMessage().contains("cantidad a reducir debe ser mayor que 0"));
        verify(productoGateway, never()).findById(anyInt());
    }

    @Test
    @DisplayName("LN06 - Debe lanzar excepción cuando no hay stock suficiente")
    void reduceStickErrorCuandoStockInsuficiente() {

        Integer cantidadAReducir = 15;

        when(productoGateway.findById(productoTest.getId()))
                .thenReturn(Optional.of(productoTest));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reduceStockProductUseCase.reduceStock(
                        productoTest.getId(),
                        cantidadAReducir
                )
        );

        assertTrue(exception.getMessage().contains("There is no stock available"),
                "El mensaje debe indicar stock insuficiente");

        verify(productoGateway, times(1)).findById(productoTest.getId());
        verify(productoGateway, never()).updateProduct(any());
        verify(productoPublisherGateway, never()).publisherStockRunningLow(any());
    }

}
