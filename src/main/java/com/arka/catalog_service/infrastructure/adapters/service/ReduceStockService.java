package com.arka.catalog_service.infrastructure.adapters.service;

import com.arka.catalog_service.domain.model.OrdenItem;
import com.arka.catalog_service.domain.useCase.WrapperReduceStockUseCase;
import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.DTO.mapper.MapperOrdenItemDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReduceStockService {

    private final WrapperReduceStockUseCase wrapperReduceStockUseCase;
    private final MapperOrdenItemDto mapper;

    public ReduceStockService(WrapperReduceStockUseCase wrapperReduceStockUseCase, MapperOrdenItemDto mapper) {
        this.wrapperReduceStockUseCase = wrapperReduceStockUseCase;
        this.mapper = mapper;
    }

    @Transactional
    public void procesarReduccion(OrdenCompleta orden){

        List<OrdenItem>items=orden.getItems().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        wrapperReduceStockUseCase.wrapper(items);
    }
}
