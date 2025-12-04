package com.delivery.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PAYMENT_STATUS_EXCHANGE = "payment.events.exchange";
    public static final String ROUTING_KEY = "payment.events.status";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";


    @Bean
    public Exchange paymentExchange() {
        return new FanoutExchange(PAYMENT_STATUS_EXCHANGE);
    }

    @Bean
    public Exchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public Binding bindingOrderCreatedQueue(Queue orderCreatedQueue, DirectExchange orderExchange) {
        return BindingBuilder
                .bind(orderCreatedQueue)
                .to(orderExchange)
                .with("order.created");
    }
}
