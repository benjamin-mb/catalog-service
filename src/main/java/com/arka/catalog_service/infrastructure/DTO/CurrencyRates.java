package com.arka.catalog_service.infrastructure.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRates {
     private Double USD;
     private Double CLP;
     private Double PEN;

}
