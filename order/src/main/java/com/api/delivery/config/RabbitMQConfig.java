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

    public static final String PAYMENT_ERROR_EXCHANGE = "payment.events.dlx";
    public static final String PAYMENT_ERROR_QUEUE = "payment.status.dlq";

    @Bean
    public TopicExchange paymentDeadLetterExchange() {
        return new TopicExchange(PAYMENT_ERROR_EXCHANGE);
    }

    @Bean
    public Queue paymentDeadLetterQueue() {
        return new Queue(PAYMENT_ERROR_QUEUE);
    }

    @Bean
    public Binding bindingDLQ(Queue paymentDeadLetterQueue, TopicExchange paymentDeadLetterExchange) {
        return BindingBuilder.bind(paymentDeadLetterQueue)
                .to(paymentDeadLetterExchange)
                .with("payment.status.#");
    }

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
        return QueueBuilder.durable(ORDER_STATUS_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_ERROR_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "payment.status.dead")
                .build();
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
