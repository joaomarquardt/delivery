package com.delivery.payment.services;

import com.delivery.payment.dtos.PaymentMessage;
import com.delivery.payment.dtos.PaymentRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentOrderListener {
    private final PaymentService paymentService;

    public PaymentOrderListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = {"order.created.queue"})
    public void processOrderPayment(PaymentMessage message) {
        PaymentRequest paymentRequest = new PaymentRequest(
                message.orderId(),
                message.paymentMethod(),
                message.value(),
                message.cardToken()
        );
        paymentService.processPayment(paymentRequest);
    }
}
