package com.arka.catalog_service.categoriaTest;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.CategoriaCreateDto;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.useCase.PostCategoriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("post categoria use case test")
public class PostCategoriaUseCaseTest {

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private PostCategoriaUseCase postCategoriaUseCase;

    private CategoriaCreateDto categoria;
    private Categorias categoriaCreada;

    @BeforeEach
    void setup() {
        categoria = new CategoriaCreateDto(
                "Electrónica",
                "Dispositivos electrónicos",
                "Tecnología"
        );

        categoriaCreada = new Categorias(
                1,
                "Electrónica",
                "Dispositivos electrónicos",
                "Tecnología",
                null
        );
    }

//    ==========================
//        CASO DE EXITO
//    ==========================

    @Test
    @DisplayName("LN01 - creacion de categoria exitosa")
    void postCategoriaUseCaeExito() {

        when(categoriaGateway.existsByNombre(categoria.getNombre())).thenReturn(false);
        when(categoriaGateway.create(any(Categorias.class))).thenReturn(categoriaCreada);

        Categorias resultado = postCategoriaUseCase.create(categoria);

        assertEquals(categoria.getNombre(), resultado.getNombre());
        assertEquals(categoria.getCaracteristicas(), resultado.getCaracteristicas());
        verify(categoriaGateway, times(1)).create(any(Categorias.class));
        verify(categoriaGateway, times(1)).existsByNombre(anyString());
    }

//    =============================
//        CASOS DE ERROR
//    =============================

    @Test
    @DisplayName("LN02 - creacion de categoria error debido  a propiedad nula, en este caso nombre")
    void PostCategoriaUseCaseErrorPropiedadNula(){

        categoria.setNombre(null);

        IllegalArgumentException exception=assertThrows(
                IllegalArgumentException.class,
                ()->postCategoriaUseCase.create(categoria)
        );

        assertEquals("nombre is obligatory",exception.getMessage());
        verify(categoriaGateway,never()).create(any(Categorias.class));

    }

    @Test
    @DisplayName("LN03 - creacion de categoria error debido  a propiedad blank, en este caso nombre")
    void postCategoriaUseCaseErrorPropiedadBlank(){

        categoria.setNombre("  ");

        IllegalArgumentException exception=assertThrows(
                IllegalArgumentException.class,
                ()->postCategoriaUseCase.create(categoria)
        );

        assertEquals("nombre is obligatory",exception.getMessage());
        verify(categoriaGateway,never()).create(any(Categorias.class));

    }

    @Test
    @DisplayName("LN04 - creacion de categoria error debido a nombre ya creado")
    void postCategoriaUseCaseErrorNombreExistente(){

        CategoriaCreateDto categoriaCreateDto=new CategoriaCreateDto(
                "Electrónica",
                "Dispositivos electrónicos unicos para productos DEll",
                "Tecnología"
        );

        when(categoriaGateway.existsByNombre(categoriaCreateDto.getNombre())).thenReturn(true);

        IllegalArgumentException exception=assertThrows(
                IllegalArgumentException.class,
                ()->postCategoriaUseCase.create(categoriaCreateDto)
        );

        assertEquals("there is already a category set with name "+categoriaCreateDto.getNombre(),exception.getMessage());
        verify(categoriaGateway,times(1)).existsByNombre(anyString());
        verify(categoriaGateway,never()).create(any(Categorias.class));
    }

}


