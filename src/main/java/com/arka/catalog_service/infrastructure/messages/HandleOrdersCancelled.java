package com.arka.catalog_service.infrastructure.messages;

import com.arka.catalog_service.infrastructure.DTO.OrdenCompleta;
import com.arka.catalog_service.infrastructure.adapters.service.IncreaseStockService;
import com.arka.catalog_service.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HandleOrdersCancelled {

    private final Logger log = LoggerFactory.getLogger(HandleOrdersCancelled.class);
    private final IncreaseStockService increaseStockService;

    public HandleOrdersCancelled(IncreaseStockService increaseStockService) {
        this.increaseStockService = increaseStockService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDERS_CANCELLED_QUEUE)
    public void handlerOrdersCancelled(OrdenCompleta ordenCompleta){
        log.info("orden recivida"+ordenCompleta);
        try {
            increaseStockService.processIncrease(ordenCompleta);
            log.info("orden procesada");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
