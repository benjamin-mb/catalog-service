package com.arka.catalog_service.categoriaTest;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.DTO.CategoriaCreateDto;
import com.arka.catalog_service.domain.model.GlobalExceptions.CategoriaNotFoundExceptions;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.useCase.UpdateDeleteCategoriaUseCase;
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
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("test de update and delet categorias ")
public class UpdateDeleteCategoriasUseCaseTest {

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private UpdateDeleteCategoriaUseCase updateDeleteCategoriaUseCase;

    private Categorias categoria;
    private Categorias categoriaActualizada;
    private CategoriaCreateDto dto;
    @BeforeEach
    void setup() {
        categoria = new Categorias(
                1,
                "Electrónica",
                "Dispositivos electrónicos",
                "Tecnologia",
                null
        );

        categoriaActualizada = new Categorias(
                1,
                "Gamer",
                "Dispositivos gamer",
                "Tecnologia Gamer",
                null
        );

        dto=new CategoriaCreateDto(
                "Gamer",
                "Dispositivos gamer",
                "Tecnologia Gamer"
        );
    }
//    ===============================
//        UPDATE CASOS EXITO
//    ===============================

    @Test
    @DisplayName("LN01 - update categoria caso de exito")
    void updateCategoriaCasoDeExito(){
        Integer id=1;
        when(categoriaGateway.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaGateway.updateCategoria(categoriaActualizada)).thenReturn(categoriaActualizada);

        Categorias resultado=updateDeleteCategoriaUseCase.update(dto,1);

        assertEquals(categoriaActualizada.getNombre(),resultado.getNombre());
        assertEquals(categoriaActualizada.getTipo(),resultado.getTipo());
        verify(categoriaGateway,times(1)).updateCategoria(any(Categorias.class));
    }

    @Test
    @DisplayName("LN02 - update categoria caso de exito pasando solo una propiedad")
    void updateCategoriaCasoDeExitoConUnaSolaPropiedad() {
        Integer id=1;

        CategoriaCreateDto dto1= new CategoriaCreateDto(
                null,
                "Cualquier caracteristica",
                null
        );
        categoriaActualizada.setCaracteristicas(dto1.getCaracteristicas());

        when(categoriaGateway.findById(id)).thenReturn(Optional.of(categoriaActualizada));
        when(categoriaGateway.updateCategoria(categoriaActualizada)).thenReturn(categoriaActualizada);

        Categorias resultado=updateDeleteCategoriaUseCase.update(dto1,id);

        assertEquals(dto1.getCaracteristicas(),resultado.getCaracteristicas());
        assertEquals(categoriaActualizada.getNombre(),resultado.getNombre());
        verify(categoriaGateway,times(1)).updateCategoria(any(Categorias.class));
    }

//    ===============================
//        UPDATE CASOS ERROR
//    ===============================

    @Test
    @DisplayName("LN03 - update categoria error debido a id no encontrado")
    void updateCategoriaCasoDeErrorIdNoEncontrado(){

        Integer id=2;
        when(categoriaGateway.findById(id)).thenReturn(Optional.empty());

        CategoriaNotFoundExceptions exceptions=assertThrows(
                CategoriaNotFoundExceptions.class,
                ()->updateDeleteCategoriaUseCase.update(dto,id)
        );
        assertEquals("category not found by id"+id,exceptions.getMessage());
        verify(categoriaGateway,never()).updateCategoria(any(Categorias.class));
    }

    @Test
    @DisplayName("LN04 - update categoria error debido a propiedad blank en este caso especifico tipo")
    void updateCategoriaCasoDeErrorPropiedadBlank(){
        Integer id=1;

        CategoriaCreateDto dto1= new CategoriaCreateDto(
                null,
                "Cualquier caracteristica",
                "   "
        );
        when(categoriaGateway.findById(id)).thenReturn(Optional.of(categoria));

        IllegalArgumentException exception=assertThrows(
                IllegalArgumentException.class,
                ()->updateDeleteCategoriaUseCase.update(dto1,id)
        );

        assertEquals("type can not be blank",exception.getMessage());
        verify(categoriaGateway,never()).updateCategoria(any(Categorias.class));
    }
}
