package com.delivery.payment.services;

import com.delivery.payment.domain.Payment;
import com.delivery.payment.domain.PaymentMethod;
import com.delivery.payment.domain.PaymentStatus;
import com.delivery.payment.dtos.PaymentRequest;
import com.delivery.payment.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(PaymentRepository paymentRepository, PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    public void processPayment(PaymentRequest paymentRequest) {
        if (paymentRequest.paymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (paymentRequest.paymentMethod() == PaymentMethod.CREDIT_CARD || paymentRequest.paymentMethod() == PaymentMethod.DEBIT_CARD) {
            if (paymentRequest.cardData() == null) {
                throw new IllegalArgumentException("Card data is required for card payments");
            }
        }
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.orderId());
        payment.setPaymentMethod(paymentRequest.paymentMethod());
        payment.setValue(paymentRequest.value());
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        paymentEventProducer.publishPaymentProcessedEvent(payment);
    }
}
