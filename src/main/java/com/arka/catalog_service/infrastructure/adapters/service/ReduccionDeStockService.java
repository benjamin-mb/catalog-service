package com.arka.catalog_service.infrastructure.adapters.service;

import com.arka.catalog_service.domain.model.OrdenItem;
import com.arka.catalog_service.domain.useCase.EnvolturaDeReduccionProductoUseCase;
import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.DTO.mapper.MapperOrdenItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReduccionDeStockService {

    private final EnvolturaDeReduccionProductoUseCase envolturaDeReduccionProductoUseCase;
    private final MapperOrdenItemDto mapper;

    public ReduccionDeStockService(EnvolturaDeReduccionProductoUseCase envolturaDeReduccionProductoUseCase, MapperOrdenItemDto mapper) {
        this.envolturaDeReduccionProductoUseCase = envolturaDeReduccionProductoUseCase;
        this.mapper = mapper;
    }

    @Transactional
    public void procesarReduccion(OrdenCompleta orden){

        List<OrdenItem>items=orden.getItems().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        envolturaDeReduccionProductoUseCase.wrapper(items);
    }
}
