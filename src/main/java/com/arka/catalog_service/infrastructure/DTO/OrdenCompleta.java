package com.arka.catalog_service.infrastructure.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCompleta {
    private List<OrdenItemDto> items;
}
