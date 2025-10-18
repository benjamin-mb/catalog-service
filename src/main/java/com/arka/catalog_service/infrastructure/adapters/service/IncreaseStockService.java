package com.arka.catalog_service.infrastructure.adapters.service;

import com.arka.catalog_service.domain.model.OrdenItem;
import com.arka.catalog_service.domain.useCase.WrapperIncreaseStockUseCase;
import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.DTO.mapper.MapperOrdenItemDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncreaseStockService {

    private final WrapperIncreaseStockUseCase wrapperIncreaseStockUseCase;
    private final MapperOrdenItemDto mapper;

    public IncreaseStockService(WrapperIncreaseStockUseCase wrapperIncreaseStockUseCase, MapperOrdenItemDto mapper) {
        this.wrapperIncreaseStockUseCase = wrapperIncreaseStockUseCase;
        this.mapper = mapper;
    }

    public void processIncrease(OrdenCompleta ordenCompleta){

        List<OrdenItem> items=ordenCompleta.getItems()
                .stream().map(mapper::toDomain)
                .collect(Collectors.toList());

        wrapperIncreaseStockUseCase.wrapper(items);
    }
}
