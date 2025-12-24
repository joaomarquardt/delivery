package com.delivery.payment.messaging.listener;

import com.delivery.payment.config.RabbitMQConfig;
import com.delivery.payment.dtos.PaymentMessage;
import com.delivery.payment.dtos.PaymentRequest;
import com.delivery.payment.services.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    private final PaymentService paymentService;

    public PaymentListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void createOrderPayment(PaymentMessage message) {
        PaymentRequest paymentRequest = new PaymentRequest(
                message.orderId(),
                message.paymentMethod(),
                message.paymentChannel(),
                message.value(),
                message.cardToken()
        );
        paymentService.createPayment(paymentRequest);
    }
}
