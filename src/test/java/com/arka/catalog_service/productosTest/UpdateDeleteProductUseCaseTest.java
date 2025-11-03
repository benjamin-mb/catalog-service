package com.arka.catalog_service.productosTest;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.useCase.UpdateDeleteProductoUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ExitCodeEvent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("prubas unitarias de update delete product Usecase")
public class UpdateDeleteProductUseCaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private UpdateDeleteProductoUseCase updateDeleteProductoUseCase;

    private Productos productoExistente;
    private Productos productoActualizado;

    @BeforeEach
    void setUp() {
        productoExistente = new Productos(
                1,
                "Laptop HP Original",
                1500,
                10,
                "Core i5, 8GB RAM",
                "HP",
                1,
                1
        );

        productoActualizado = new Productos(
                1,
                "Laptop HP Actualizada",
                1800,
                15,
                "Core i7, 16GB RAM",
                "HP",
                1,
                1
        );
    }

//    ===========================
//        UPDATE PRODUCT EXITO
//    ===========================

    @Test
    @DisplayName("LN01 - Debe actualizar producto correctamente cuando existe")
    void updateProductExito() {

        when(productoGateway.findById(productoActualizado.getId()))
                .thenReturn(Optional.of(productoExistente));

        when(categoriaGateway.existsById(productoActualizado.getCategoria()))
                .thenReturn(true);

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenReturn(productoActualizado);

        Productos resultado = updateDeleteProductoUseCase.update(productoActualizado);

        assertEquals(productoActualizado.getNombre(), resultado.getNombre());
        assertEquals(productoActualizado.getPrecio(), resultado.getPrecio());

        verify(productoGateway, times(1)).findById(productoActualizado.getId());
        verify(productoGateway, times(1)).updateProduct(any(Productos.class));
    }

    @Test
    @DisplayName("LN02 - Debe actualizar solo el nombre cuando es lo único que cambia")
    void updateProductExitoSoloNombre() {

        Productos productoConNombreNuevo = new Productos();
        productoConNombreNuevo.setId(1);
        productoConNombreNuevo.setNombre("Nuevo Nombre");

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Productos resultado = updateDeleteProductoUseCase.update(productoConNombreNuevo);

        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals(productoExistente.getPrecio(), resultado.getPrecio());
        assertEquals(productoExistente.getStock(), resultado.getStock());
        assertEquals(productoExistente.getMarca(), resultado.getMarca());
    }

    @Test
    @DisplayName("LN03 - Debe actualizar solo el precio cuando es lo único que cambia")
    void updateProductExitoSoloPrecio() {

        Productos productoConPrecioNuevo = new Productos();
        productoConPrecioNuevo.setId(1);
        productoConPrecioNuevo.setPrecio(2000);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Productos resultado = updateDeleteProductoUseCase.update(productoConPrecioNuevo);

        assertEquals(2000, resultado.getPrecio());
        assertEquals(productoExistente.getNombre(), resultado.getNombre());
    }

    @Test
    @DisplayName("LN04 - Debe actualizar solo el stock cuando es lo único que cambia")
    void updateProductExitoSoloStock() {

        Productos productoConStockNuevo = new Productos();
        productoConStockNuevo.setId(1);
        productoConStockNuevo.setStock(20);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Productos resultado = updateDeleteProductoUseCase.update(productoConStockNuevo);

        assertEquals(20, resultado.getStock());
        assertEquals(productoExistente.getNombre(), resultado.getNombre());
    }

    @Test
    @DisplayName("LN05 - Debe actualizar solo la marca cuando es lo único que cambia")
    void updateProductExitoSoloMarca() {

        Productos productoConMarcaNueva = new Productos();
        productoConMarcaNueva.setId(1);
        productoConMarcaNueva.setMarca("Dell");

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Productos resultado = updateDeleteProductoUseCase.update(productoConMarcaNueva);

        assertEquals("Dell", resultado.getMarca());
        assertEquals(productoExistente.getNombre(), resultado.getNombre());
    }

    @Test
    @DisplayName("LN06 - Debe actualizar solo la categoría cuando es válida")
    void updateProductExitoSoloCategoria() {

        Integer nuevaCategoria = 2;
        Productos productoConCategoriaNueva = new Productos();
        productoConCategoriaNueva.setId(1);
        productoConCategoriaNueva.setCategoria(nuevaCategoria);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(categoriaGateway.existsById(nuevaCategoria))
                .thenReturn(true);

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Productos resultado = updateDeleteProductoUseCase.update(productoConCategoriaNueva);

        assertEquals(nuevaCategoria, resultado.getCategoria());
        verify(categoriaGateway, times(1)).existsById(nuevaCategoria);
    }

    @Test
    @DisplayName("LN07 - Debe actualizar múltiples campos a la vez")
    void updateProductExitoVariasFields() {

        Productos productoConCambiosMultiples = new Productos();
        productoConCambiosMultiples.setId(1);
        productoConCambiosMultiples.setNombre("Nuevo Nombre");
        productoConCambiosMultiples.setPrecio(2500);
        productoConCambiosMultiples.setStock(30);
        productoConCambiosMultiples.setMarca("Lenovo");

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Productos resultado = updateDeleteProductoUseCase.update(productoConCambiosMultiples);

        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals(2500, resultado.getPrecio());
        assertEquals(30, resultado.getStock());
        assertEquals("Lenovo", resultado.getMarca());
    }

//    ===============================
//        UPDATE PRODUCT ERRORES
//    ===============================

    @Test
    @DisplayName("LN08 - Debe lanzar excepción cuando el producto no existe")
    void UpdateProductErrorProductoNoExiste() {

        Productos productoInexistente = new Productos();
        productoInexistente.setId(999);
        productoInexistente.setNombre("Test");

        when(productoGateway.findById(999))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> updateDeleteProductoUseCase.update(productoInexistente)
        );

        assertTrue(exception.getMessage().contains("No product has been found with id:999"));

        verify(productoGateway, times(1)).findById(999);
        verify(productoGateway, never()).updateProduct(any());
    }

    @Test
    @DisplayName("LN09 - Debe lanzar excepción cuando el precio es negativo")
    void UpdateProductErrorPrecioMenorA1() {

        Productos productoConPrecioNegativo = new Productos();
        productoConPrecioNegativo.setId(1);
        productoConPrecioNegativo.setPrecio(-100);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateDeleteProductoUseCase.update(productoConPrecioNegativo)
        );

        assertTrue(exception.getMessage().contains("price can't be negative"));
        verify(productoGateway, never()).updateProduct(any());
    }

    @Test
    @DisplayName("LN10 - Debe lanzar excepción cuando el stock es negativo")
    void UpdateProductErrorStockMenorA1() {

        Productos productoConStockNegativo = new Productos();
        productoConStockNegativo.setId(1);
        productoConStockNegativo.setStock(-5);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateDeleteProductoUseCase.update(productoConStockNegativo)
        );

        assertTrue(exception.getMessage().contains("stock can't be negative"));
        verify(productoGateway, never()).updateProduct(any());
    }

    @Test
    @DisplayName("LN11 - Debe lanzar excepción cuando la categoría no existe")
    void UpdateProductErrorCategoriaInexistente() {

        Integer categoriaInexistente = 999;
        Productos productoConCategoriaInvalida = new Productos();
        productoConCategoriaInvalida.setId(1);
        productoConCategoriaInvalida.setCategoria(categoriaInexistente);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(categoriaGateway.existsById(categoriaInexistente))
                .thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> updateDeleteProductoUseCase.update(productoConCategoriaInvalida)
        );

        assertTrue(exception.getMessage().contains("category id" + categoriaInexistente));
        assertTrue(exception.getMessage().contains("do not exist"));

        verify(categoriaGateway, times(1)).existsById(categoriaInexistente);
        verify(productoGateway, never()).updateProduct(any());
    }

    @Test
    @DisplayName("LN12 - No debe actualizar cuando todos los campos son null")
    void UpdateProductErrorTodasLasFieldsVacias() {
        Productos productoSinCambios = new Productos();
        productoSinCambios.setId(1);

        when(productoGateway.findById(1))
                .thenReturn(Optional.of(productoExistente));

        when(productoGateway.updateProduct(any(Productos.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Productos resultado = updateDeleteProductoUseCase.update(productoSinCambios);

        assertEquals(productoExistente.getNombre(), resultado.getNombre());
        assertEquals(productoExistente.getPrecio(), resultado.getPrecio());
        assertEquals(productoExistente.getStock(), resultado.getStock());
        assertEquals(productoExistente.getMarca(), resultado.getMarca());
        assertEquals(productoExistente.getCategoria(), resultado.getCategoria());
    }

//    ===========================
//          DELETE EXITOSO
//    ===========================

    @Test
    @DisplayName("LN13 - Debe eliminar producto correctamente cuando existe")
    void deleteProductExito() {

        Integer idProducto = 1;
        when(productoGateway.findById(idProducto))
                .thenReturn(Optional.of(productoExistente));

        Productos resultado = updateDeleteProductoUseCase.delete(idProducto);

        assertNotNull(resultado);
        assertEquals(productoExistente.getId(), resultado.getId());
        assertEquals(productoExistente.getNombre(), resultado.getNombre());

        verify(productoGateway, times(1)).findById(idProducto);
        verify(productoGateway, times(1)).deleteById(idProducto);
    }

//    ====================
//        DELETE ERROR
//    ====================

    @Test
    @DisplayName("LN14 - Debe lanzar excepción cuando el producto a eliminar no existe")
    void deleteProductError() {

        Integer idInexistente = 999;
        when(productoGateway.findById(idInexistente))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                    EntityNotFoundException.class,
                () -> updateDeleteProductoUseCase.delete(idInexistente)
        );

        assertTrue(exception.getMessage().contains("No product has been found with id:" + idInexistente));

        verify(productoGateway, times(1)).findById(idInexistente);
        verify(productoGateway, never()).deleteById(anyInt());
    }

}