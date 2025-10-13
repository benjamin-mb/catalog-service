package com.arka.catalog_service.infrastructure.messages;

import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.adapters.service.ReduccionDeStockService;
import com.arka.catalog_service.infrastructure.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HandleOrdersCreated {

    private final ReduccionDeStockService reduccionDeStockService;

    public HandleOrdersCreated(ReduccionDeStockService reduccionDeStockService) {
        this.reduccionDeStockService = reduccionDeStockService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrdenCompleta orden){
        System.out.println("🔔 Evento recibido: " + orden);
        System.out.println("🔔 Items: " + (orden.getItems() != null ? orden.getItems().size() : "NULL"));

        try{
            reduccionDeStockService.procesarReduccion(orden);
            System.out.println("✅ Orden procesada");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
