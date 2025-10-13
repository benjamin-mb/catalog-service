package com.arka.catalog_service.infrastructure.DTO.mapper;

import com.arka.catalog_service.domain.model.OrdenItem;
import com.arka.catalog_service.infrastructure.DTO.OrdenItemDto;
import org.springframework.stereotype.Component;

@Component
public class MapperOrdenItemDto {
    public OrdenItem toDomain (OrdenItemDto dto){
        OrdenItem domain=new OrdenItem(
                dto.getIdProducto(),
                dto.getCantidad()
        );
        return domain;
    }
}
