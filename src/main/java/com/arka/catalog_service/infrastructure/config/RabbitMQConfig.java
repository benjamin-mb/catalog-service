package com.arka.catalog_service.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {

    //  Constantes con nombres de exchange, queue y routing keys

    //Listener reduce stock
    public static final String ORDERS_EXCHANGE = "orders.exchange"; // donde publica el micro de ordenes
    public static final String ORDER_CREATED_QUEUE = "order.created.inventory"; // cola para catálogo
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";
    //Dead letterreduce stock
    public static final String ORDER_CREATED_DLQ="order.created.inventory.dlq";
    public static final String ORDER_CREATED_DLX="order.created.inventory.dlx";

    //Constantes de retry
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_INITIAL_INTERVAL = 2000L;
    private static final double RETRY_MULTIPLIER = 2.0;
    private static final long RETRY_MAX_INTERVAL = 10000L;

    //Publihser notificacion
    public static final String NOTIFICATIONS_EXCHANGE = "notifications.exchange"; // donde publica catálogo
    public static final String STOCK_LOW_ROUTING_KEY = "stock.low";

    //Exchange para órdenes (tipo Topic: enrutamiento flexible con patrones)
    @Bean
    public TopicExchange ordersExchange() {
        return new TopicExchange(ORDERS_EXCHANGE);
    }

    @Bean
    public DirectExchange orderCreatedDlx(){
        return new DirectExchange(ORDER_CREATED_DLX);
    }

    //Exchange para notificaciones
    @Bean
    public TopicExchange notificationsExchange() {
        return new TopicExchange(NOTIFICATIONS_EXCHANGE);
    }

    //Cola para escuchar las órdenes creadas
    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(ORDER_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_CREATED_DLX)
                .withArgument("x-dead-letter-routing-key", ORDER_CREATED_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderCreatedDlq() {
        return QueueBuilder.durable(ORDER_CREATED_DLQ).build();
    }

    //Binding: conecta el exchange de órdenes con la cola
    @Bean
    public Binding bindingOrderCreated(Queue orderCreatedQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(orderCreatedQueue)
                .to(ordersExchange)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingOrderCreatedDlq(Queue orderCreatedDlq,DirectExchange orderCreatedDlx){
        return BindingBuilder
                .bind(orderCreatedDlq)
                .to(orderCreatedDlx)
                .with(ORDER_CREATED_ROUTING_KEY);
    }

    //Message converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //RabbitTemplate: usado para enviar eventos (ej. stock bajo)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template= new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(MAX_RETRY_ATTEMPTS)
                .backOffOptions(
                        RETRY_INITIAL_INTERVAL,
                        RETRY_MULTIPLIER,
                        RETRY_MAX_INTERVAL
                )
                .build();
    }
}

