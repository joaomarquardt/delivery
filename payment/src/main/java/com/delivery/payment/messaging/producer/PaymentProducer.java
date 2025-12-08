package com.delivery.payment.messaging.producer;

import com.delivery.payment.config.RabbitMQConfig;
import com.delivery.payment.dtos.PaymentStatusMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class PaymentProducer {
    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentStatusMessage(Long orderId, boolean success) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_STATUS_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                new PaymentStatusMessage(orderId, success));
    }
}
