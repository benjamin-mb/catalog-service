package com.arka.catalog_service.infrastructure.messages;

import com.arka.catalog_service.infrastructure.config.RabbitMQConfig;
import com.arka.catalog_service.infrastructure.messages.DTO.ProductsRunningLowStock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PublisherProducto {

    private final RabbitTemplate rabbitTemplate;

    public PublisherProducto(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publisherStockRunningLow(ProductsRunningLowStock eventDto){
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATIONS_EXCHANGE,
                    RabbitMQConfig.STOCK_LOW_ROUTING_KEY,
                    eventDto
            );
        System.out.println("📢 Published low stock event: " + eventDto);
    }
}
