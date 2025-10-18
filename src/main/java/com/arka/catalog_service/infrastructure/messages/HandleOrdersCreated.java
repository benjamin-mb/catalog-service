package com.arka.catalog_service.infrastructure.messages;

import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.adapters.service.ReduceStockService;
import com.arka.catalog_service.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HandleOrdersCreated {

    private final ReduceStockService reduceStockService;

    public HandleOrdersCreated(ReduceStockService reduceStockService) {
        this.reduceStockService = reduceStockService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrdenCompleta orden){
        try{
            reduceStockService.procesarReduccion(orden);
            System.out.println("Orden procesada");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
