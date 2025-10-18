package com.arka.catalog_service.infrastructure.adapters.Component;

import com.arka.catalog_service.infrastructure.DTO.CurrencyRates;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class CurrenciesService {

    private  final RestClient restClient;

    public CurrenciesService(RestClient restClient) {
        this.restClient = restClient;
    }
    @Cacheable(value = "currencies", key = "'rates'")
    public CurrencyRates getCurrencies(){
        Map<String,Object>response= restClient.get()
                .uri("/e5711abb6046eaab04868f49/latest/COP")
                .retrieve()
                .body(Map.class);
        if (response == null || !response.containsKey("conversion_rates")){
            throw  new RuntimeException("currencies could not be loaded");
        }
        Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");

        Double USD = convertToDouble(rates.get("USD"));
        Double CLP = convertToDouble(rates.get("CLP"));
        Double PEN = convertToDouble(rates.get("PEN"));

        return new CurrencyRates(USD,CLP,PEN);
    }

    private Double convertToDouble(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }
}
