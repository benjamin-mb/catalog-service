package com.arka.catalog_service.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;

/**
 * Configuración de RabbitMQ para el microservicio de catálogo.
 * Aquí declaramos:
 *  - Exchanges (puentes de eventos)
 *  - Queues (colas donde se guardan los mensajes)
 *  - Bindings (cómo se conectan exchanges con queues)
 *  - RabbitTemplate (para publicar mensajes)
 */
@Configuration
public class RabbitMQConfig {

    //  Constantes con nombres de exchange, queue y routing keys
    public static final String ORDERS_EXCHANGE = "orders.exchange"; // donde publica el micro de ordenes
    public static final String ORDER_CREATED_QUEUE = "order.created.inventory"; // cola para catálogo
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String NOTIFICATIONS_EXCHANGE = "notifications.exchange"; // donde publica catálogo
    public static final String STOCK_LOW_ROUTING_KEY = "stock.low";

    // 🔹 Exchange para órdenes (tipo Topic: enrutamiento flexible con patrones)
    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ORDERS_EXCHANGE);
    }

    // 🔹 Exchange para notificaciones
    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(NOTIFICATIONS_EXCHANGE);
    }

    // 🔹 Cola para escuchar las órdenes creadas
    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true); // true = persistente
    }

    // 🔹 Binding: conecta el exchange de órdenes con la cola
    @Bean
    public Binding bindingOrderCreated(Queue orderCreatedQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(ordersExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    // 🔹 RabbitTemplate: usado para enviar eventos (ej. stock bajo)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}

