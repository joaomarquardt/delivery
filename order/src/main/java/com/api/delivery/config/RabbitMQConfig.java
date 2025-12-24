package com.api.delivery.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String PAYMENT_EVENTS_EXCHANGE = "payment.events.exchange";
    public static final String ORDER_STATUS_QUEUE = "order.status.queue";
    public static final String PAYMENT_STATUS_ROUTING_PATTERN = "payment.status.#";

    @Bean
    public Exchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Exchange paymentStatusExchange() {
        return new TopicExchange(PAYMENT_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue orderStatusQueue() {
        return new Queue(ORDER_STATUS_QUEUE);
    }

    @Bean
    public Binding bindingOrderQueueToPaymentEvents(Queue orderStatusQueue, TopicExchange paymentStatusExchange) {
        return BindingBuilder
                .bind(orderStatusQueue)
                .to(paymentStatusExchange)
                .with(PAYMENT_STATUS_ROUTING_PATTERN);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
