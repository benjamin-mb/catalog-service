package com.arka.catalog_service.productosTest;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.useCase.GetterProductUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("test para GetterProductUseCase")
public class GetterProductUseCaseTest {

    @Mock
    private ProductoGateway productoGateway;

    @InjectMocks
    private GetterProductUseCase getterProductUseCase;

    private Productos producto1;
    private Productos producto2;
    private Productos producto3;

    @BeforeEach
    void setUp() {
        producto1 = new Productos(
                1, "Laptop HP", 1500, 10,
                "Core i5, 8GB RAM", "HP", 1, 1
        );

        producto2 = new Productos(
                2, "Mouse Logitech", 50, 25,
                "Inalámbrico", "Logitech", 2, 1
        );

        producto3 = new Productos(
                3, "Teclado Logitech", 80, 15,
                "Mecánico RGB", "Logitech", 2, 1
        );
    }

//    =========================
//            GET BY ID
//    =========================

    @Test
    @DisplayName("LN01 - Debe obtener producto por ID cuando existe")
    void obtenerProductoByidExitoso() {
        Integer idProducto = 1;
        when(productoGateway.findById(idProducto))
                .thenReturn(Optional.of(producto1));

        Productos resultado = getterProductUseCase.getById(idProducto);

        assertNotNull(resultado);
        assertEquals(producto1.getId(), resultado.getId());
        assertEquals(producto1.getNombre(), resultado.getNombre());

        verify(productoGateway, times(1)).findById(idProducto);
    }

    @Test
    @DisplayName("LN02 - Debe lanzar EntityNotFoundException cuando producto no existe por ID")
    void obtenerProductoByIdErrorIDinexistente() {

        Integer idInexistente = 999;
        when(productoGateway.findById(idInexistente))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> getterProductUseCase.getById(idInexistente)
        );

        assertTrue(exception.getMessage().contains("product with id:" + idInexistente));
        assertTrue(exception.getMessage().contains("was not found"));

        verify(productoGateway, times(1)).findById(idInexistente);
    }

//    =============================
//            GET BY NOMBRE
//    =============================
    @Test
    @DisplayName("LN03 - Debe obtener producto por nombre cuando existe")
    void obtenerProductoByNombre() {

        String nombreProducto = "Laptop HP";
        when(productoGateway.findByNombre(nombreProducto))
                .thenReturn(Optional.of(producto1));

        Productos resultado = getterProductUseCase.getByNombre(nombreProducto);

        assertNotNull(resultado);
        assertEquals(nombreProducto, resultado.getNombre());

        verify(productoGateway, times(1)).findByNombre(nombreProducto);
    }

    @Test
    @DisplayName("LN04 - Debe lanzar error cuando producto no existe por nombre")
    void obtenerProductoByNombreError() {

        String nombreInexistente = "Producto Inexistente";
        when(productoGateway.findByNombre(nombreInexistente))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> getterProductUseCase.getByNombre(nombreInexistente)
        );

        assertTrue(exception.getMessage().contains("producto no encontrado con nombre"));
        assertTrue(exception.getMessage().contains(nombreInexistente));

        verify(productoGateway, times(1)).findByNombre(nombreInexistente);
    }

//    ==========================
//            GET BY MARCA
//    ==========================

    @Test
    @DisplayName("LN05 - Debe obtener lista de productos por marca cuando existen")
    void obtenerListaDeProductosByMarca() {

        String marca = "Logitech";
        List<Productos> productosLogitech = Arrays.asList(producto2, producto3);

        when(productoGateway.findAllByMarca(marca))
                .thenReturn(productosLogitech);

        List<Productos> resultado = getterProductUseCase.getByMarca(marca);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getMarca().equals(marca)));

        verify(productoGateway, times(1)).findAllByMarca(marca);
    }

    @Test
    @DisplayName("LN06 - Debe lanzar excepción cuando no hay productos de esa marca")
    void obtenerListDeProductosByMarcaErrorListaVacia() {

        String marcaSinProductos = "MarcaInexistente";
        when(productoGateway.findAllByMarca(marcaSinProductos))
                .thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> getterProductUseCase.getByMarca(marcaSinProductos)
        );

        assertTrue(exception.getMessage().contains("there are no products with that brand"));

        verify(productoGateway, times(1)).findAllByMarca(marcaSinProductos);
    }

//    ===========================
//        GET BY CATEGORIA
//    ===========================

    @Test
    @DisplayName("LN07 - Debe obtener productos por categoría")
    void obtenerListaDeProductosByCategoria() {

        Integer idCategoria = 2;
        List<Productos> productosCategoria2 = Arrays.asList(producto2, producto3);

        when(productoGateway.findAllByCategoria(idCategoria))
                .thenReturn(productosCategoria2);

        List<Productos> resultado = getterProductUseCase.getByCategoria(idCategoria);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getCategoria().equals(idCategoria)));

        verify(productoGateway, times(1)).findAllByCategoria(idCategoria);
    }

    @Test
    @DisplayName("LN08 - Debe lanzar excepción cuando no hay productos en esa categoría")
    void obtenerProductosByCategoriaError() {

        Integer categoriaSinProductos = 999;
        when(productoGateway.findAllByCategoria(categoriaSinProductos))
                .thenReturn(Collections.emptyList());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> getterProductUseCase.getByCategoria(categoriaSinProductos)
        );

        assertTrue(exception.getMessage().contains("there are no products with that category"));

        verify(productoGateway, times(1)).findAllByCategoria(categoriaSinProductos);
    }

    @Test
    @DisplayName("LN09 - Debe obtener todos los productos cuando existen")
    void obtenerTodosLosProductos() {

        List<Productos> todosLosProductos = Arrays.asList(producto1, producto2, producto3);
        when(productoGateway.findAll())
                .thenReturn(todosLosProductos);

        List<Productos> resultado = getterProductUseCase.getAll();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.containsAll(todosLosProductos));

        verify(productoGateway, times(1)).findAll();
    }
}
