package com.arka.catalog_service.infrastructure.messages;

import com.arka.catalog_service.infrastructure.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
public class HandleOrdersCreated {


}
