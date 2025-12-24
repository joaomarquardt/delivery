package com.delivery.payment.messaging.producer;

import com.delivery.payment.config.RabbitMQConfig;
import com.delivery.payment.dtos.PaymentStatusMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentProducer {
    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentStatusMessage(Long orderId, boolean success) {
        String statusKey = success ? "payment.status.approved" : "payment.status.failed";
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EVENTS_EXCHANGE,
                statusKey,
                new PaymentStatusMessage(orderId, success));
    }
}
