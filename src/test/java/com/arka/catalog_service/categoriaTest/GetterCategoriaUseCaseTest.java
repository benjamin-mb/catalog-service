package com.arka.catalog_service.categoriaTest;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.model.GlobalExceptions.CategoriaNotFoundExceptions;
import com.arka.catalog_service.domain.model.gateway.CategoriaGateway;
import com.arka.catalog_service.domain.useCase.GetterCategoriaUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("getter producto use case test")
public class GetterCategoriaUseCaseTest {

    @Mock
    private CategoriaGateway categoriaGateway;

    @InjectMocks
    private GetterCategoriaUseCase categoriaUseCase;

    private Categorias categoria1;
    private Categorias categoria2;
    private Categorias categoria3;

    @BeforeEach
    void setup(){
        categoria1 = new Categorias(
                1,
                "Electrónica",
                "Dispositivos electrónicos",
                "Tecnologia",
                null
        );

        categoria2= new Categorias(
                3,
                "Gamer",
                "Dispositivos gamer",
                "Tecnologia",
                null
        );

        categoria3 = new Categorias(
                1,
                "repuestos gamer",
                "reouestos de disposivos electrónicos",
                "Repuestos",
                null
        );
    }

//    ============================
//            GET BY ID
//    ============================
    @Test
    @DisplayName("LN01 - get categoria by id exitoso")
    void getCategoriaByIdExito(){
        Integer id=1;
        when(categoriaGateway.findById(categoria1.getId())).thenReturn(Optional.of(categoria1));

        Categorias resultado=categoriaUseCase.getById(1);

        assertEquals(categoria1.getNombre(),resultado.getNombre());
        assertEquals(categoria1.getTipo(),resultado.getTipo());
        verify(categoriaGateway,times(1)).findById(anyInt());

    }

    @Test
    @DisplayName("LN02 - get categoria by id error")
    void getCategoriaByIdError(){
        Integer id=899;
        when(categoriaGateway.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception=assertThrows(
                EntityNotFoundException.class,
                ()->categoriaUseCase.getById(id)
        );

        assertEquals("no category has been found with id "+id, exception.getMessage());
        verify(categoriaGateway,times(1)).findById(anyInt());
    }

//    =======================
//        GET BY NAME
//    =======================

    @Test
    @DisplayName("LN03- get by name caso exitoso")
    void getCategoriaByNameExitoso(){
        String nombre="Gamer";
        when(categoriaGateway.findByNombre(nombre)).thenReturn(Optional.of(categoria2));

        Categorias resultado=categoriaUseCase.getByName(nombre);

        assertEquals(categoria2.getNombre(),resultado.getNombre());
        verify(categoriaGateway,times(1)).findByNombre(anyString());
    }

    @Test
    @DisplayName("LN04 - get categoria by name error")
    void  getCategoryByNameError(){

        String nombreInexistente="DELL";
        when(categoriaGateway.findByNombre(nombreInexistente)).thenReturn(Optional.empty());

        CategoriaNotFoundExceptions exception=assertThrows(
                CategoriaNotFoundExceptions.class,
                ()->categoriaUseCase.getByName(nombreInexistente)
        );

        assertEquals("no category has been found with name:" +nombreInexistente,exception.getMessage());
        verify(categoriaGateway,times(1)).findByNombre(anyString());
    }

//    =======================
//        GET  ALL BY TIPO
//    =======================

    @Test
    @DisplayName("LN05 - get categoriaas by tipo exito")
    void getListCategoryByTipo(){

        String tipo="Tecnologia";
        List<Categorias>categoriasList= Arrays.asList(categoria1,categoria2);

        when(categoriaGateway.findAllByTipo(tipo)).thenReturn(categoriasList);

        List<Categorias>categorias=categoriaUseCase.getAllByTipo(tipo);

        assertEquals(2,categorias.size());
        assertNotNull(categorias);
        verify(categoriaGateway, times(1)).findAllByTipo(anyString());
    }

    @Test
    @DisplayName("LN06 - get categorias by tipo error")
    void getListCategoryByTipoError(){

        String tipo="Gamers";

        when(categoriaGateway.findAllByTipo(tipo)).thenReturn(Collections.emptyList());

        CategoriaNotFoundExceptions exceptions=assertThrows(
                CategoriaNotFoundExceptions.class,
                ()->categoriaUseCase.getAllByTipo(tipo)
        );

        assertEquals("there are no categories with that tipo",exceptions.getMessage());
        verify(categoriaGateway, times(1)).findAllByTipo(anyString());
    }

//    =======================
//            GET ALL
//    =======================

    @Test
    @DisplayName("LN07 - obtener todas las categorias")
    void getAll(){
        List<Categorias>categoriasList= Arrays.asList(categoria1,categoria2,categoria3);

        when(categoriaGateway.findAll()).thenReturn(categoriasList);

        List<Categorias>categorias=categoriaGateway.findAll();

        assertEquals(categoriasList,categorias);
        verify(categoriaGateway,times(1)).findAll();
    }


}
