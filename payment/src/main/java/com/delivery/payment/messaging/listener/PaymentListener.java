package com.delivery.payment.messaging.listener;

import com.delivery.payment.config.RabbitMQConfig;
import com.delivery.payment.dtos.PaymentMessage;
import com.delivery.payment.dtos.PaymentRequest;
import com.delivery.payment.messaging.producer.PaymentProducer;
import com.delivery.payment.services.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;

    public PaymentListener(PaymentService paymentService, PaymentProducer paymentProducer) {
        this.paymentService = paymentService;
        this.paymentProducer = paymentProducer;
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
        try {
            paymentService.validatePaymentRequest(paymentRequest);
            paymentService.createPayment(paymentRequest);
        } catch (Exception e) {
            paymentProducer.sendPaymentStatusMessage(message.orderId(), false);
        }
    }
}
