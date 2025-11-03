package com.arka.catalog_service.productosTest;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.ProductoCreateDto;
import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.model.gateway.ProductoGateway;
import com.arka.catalog_service.domain.model.gateway.ProvedorGateway;
import com.arka.catalog_service.domain.useCase.PostProductUseCase;
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
public class PostProductUseCaseTest {

    @Mock
    private CategoriaGateway categoriaGateway;

    @Mock
    private ProductoGateway productoGateway;

    @Mock
    private ProvedorGateway provedorGateway;

    @InjectMocks
    private PostProductUseCase postProductUseCase;

    private ProductoCreateDto productoCreateDto;
    private Categorias categoria;

    @BeforeEach
    void setUp() {
        productoCreateDto = new ProductoCreateDto(
        "Laptop HP Pavilion" ,
        1500,
        10,
        "Core i5, 8GB RAM, 256GB SSD",
        "HP",
        1,
        1);

        categoria = new Categorias(
                1,
                "Electrónica",
                "Dispositivos electrónicos",
                "Tecnología",
                null
        );
    }

//    ====================================
//            CASOS DE EXITO
//    ====================================
    @Test
    @DisplayName("LN01 -  creacion de producto exitosa")
    void creacionDeProductoExitosa(){

        when(categoriaGateway.findById(productoCreateDto.getCategoria())).thenReturn(Optional.of(categoria));

        when(productoGateway.existsByNombreAndCategoria(
                productoCreateDto.getNombre(),
                productoCreateDto.getCategoria()))
                .thenReturn(false);

        when(productoGateway.existsByNombre(productoCreateDto.getNombre()))
                .thenReturn(false);

        when(provedorGateway.existsById(productoCreateDto.getProveedor()))
                .thenReturn(true);

        Productos productoGuardado = new Productos(
                1,
                productoCreateDto.getNombre(),
                productoCreateDto.getPrecio(),
                productoCreateDto.getStock(),
                productoCreateDto.getCaracteristicas(),
                productoCreateDto.getMarca(),
                productoCreateDto.getCategoria(),
                productoCreateDto.getProveedor()
        );
        when(productoGateway.save(any(Productos.class)))
                .thenReturn(productoGuardado);

        Productos resultado = postProductUseCase.create(productoCreateDto);

        assertEquals(productoCreateDto.getNombre(),resultado.getNombre());
        assertEquals(productoCreateDto.getPrecio(), resultado.getPrecio());
        assertEquals(productoCreateDto.getStock(), resultado.getStock());

        verify(categoriaGateway, times(1)).findById(productoCreateDto.getCategoria());
        verify(productoGateway, times(1)).save(any(Productos.class));
    }

//    =================================
//            CASOS DE ERROR
//    =================================

    @Test
    @DisplayName("LN02 - Debe lanzar excepción cuando la categoría no existe")
    void createProductErrorCategoriaInexistente() {

        when(categoriaGateway.findById(productoCreateDto.getCategoria()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("la categoria"));

        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN03 - Debe lanzar excepción cuando el stock es 0")
    void createProductErrorStockMenorACerooIgual() {

        //debe lanzar error con stock 0 y menor a 0
        productoCreateDto.setStock(0);

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("stock can't be below 0"));
        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN04 - Debe lanzar excepción cuando el producto ya existe en la categoría")
    void createProductErrorNombreYaRegistradoEnCategoria() {

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));

        when(productoGateway.existsByNombreAndCategoria(
                productoCreateDto.getNombre(),
                productoCreateDto.getCategoria()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("the product already exists on the category"));
        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN05 - Debe lanzar excepción cuando el precio es negativo")
    void createProductErrorPrecioNegativo() {

        productoCreateDto.setPrecio(-100);

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));
        when(productoGateway.existsByNombreAndCategoria(anyString(), anyInt()))
                .thenReturn(false);
        when(productoGateway.existsByNombre(anyString()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("the product has to have a price"));
        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN06 - Debe lanzar excepción cuando una propiedad es null en este caso la prueba es con caracteristica")
    void createProductErrorCuandoUnaPropiedadEsNull() {

        productoCreateDto.setCaracteristicas(null);

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));
        when(productoGateway.existsByNombreAndCategoria(anyString(), anyInt()))
                .thenReturn(false);
        when(productoGateway.existsByNombre(anyString()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("caracteristics field can not be blank"));
        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN07 - Debe lanzar excepción cuando una propiedad está en blanco en este caso caracteristicas")
    void givenCaracteristicasBlank_whenCreate_thenThrowsIllegalArgumentException() {

        productoCreateDto.setCaracteristicas("   ");

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));
        when(productoGateway.existsByNombreAndCategoria(anyString(), anyInt()))
                .thenReturn(false);
        when(productoGateway.existsByNombre(anyString()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("caracteristics field can not be blank"));
        verify(productoGateway, never()).save(any());
    }

    @Test
    @DisplayName("LN07 - Debe lanzar excepción cuando el proveedor no existe")
    void productCreateErrorProveedorInexistente() {

        when(categoriaGateway.findById(anyInt()))
                .thenReturn(Optional.of(categoria));
        when(productoGateway.existsByNombreAndCategoria(anyString(), anyInt()))
                .thenReturn(false);
        when(productoGateway.existsByNombre(anyString()))
                .thenReturn(false);

        when(productoGateway.existsById(productoCreateDto.getProveedor()))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postProductUseCase.create(productoCreateDto)
        );

        assertTrue(exception.getMessage().contains("proveedor with id"));
        assertTrue(exception.getMessage().contains("has not been found"));
        verify(productoGateway, never()).save(any());
    }
}
