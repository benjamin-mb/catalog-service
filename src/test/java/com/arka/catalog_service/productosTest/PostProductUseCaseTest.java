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
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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
        productoCreateDto = new ProductoCreateDto();
        productoCreateDto.setNombre("Laptop HP Pavilion");
        productoCreateDto.setPrecio(1500);
        productoCreateDto.setStock(10);
        productoCreateDto.setCaracteristicas("Core i5, 8GB RAM, 256GB SSD");
        productoCreateDto.setMarca("HP");
        productoCreateDto.setCategoria(1);
        productoCreateDto.setProveedor(1);

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

        // When
        Productos resultado = postProductUseCase.create(productoCreateDto);

        // Then
        assertNotNull(resultado);
        assertEquals(productoCreateDto.getNombre(),resultado.getNombre());
        assertEquals(productoCreateDto.getPrecio(), resultado.getPrecio());
        assertEquals(productoCreateDto.getStock(), resultado.getStock());

        verify(categoriaGateway, times(1)).findById(productoCreateDto.getCategoria());
        verify(productoGateway, times(1)).save(any(Productos.class));
    }
}
