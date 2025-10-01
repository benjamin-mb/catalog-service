package com.arka.catalog_service.infrastructure.adapters.Component;

import com.arka.catalog_service.infrastructure.adapters.Component.Dto.CurrencyRates;
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
        if (response == null || response.containsKey("conversion_rates")){
            throw  new RuntimeException("currencies could not be loaded");
        }
        Map<String,Double>rates=(Map<String,Double>)response.get("conversion_rates");
        Double USD = (Double) rates.get("USD");
        Double CLP = (Double) rates.get("CLP");
        Double PEN = (Double) rates.get("PEN");

        return new CurrencyRates(USD,CLP,PEN);
    }
}
