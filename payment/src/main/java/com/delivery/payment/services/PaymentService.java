package com.delivery.payment.services;

import com.delivery.payment.domain.Payment;
import com.delivery.payment.enums.PaymentMethod;
import com.delivery.payment.enums.PaymentStatus;
import com.delivery.payment.dtos.PaymentRequest;
import com.delivery.payment.enums.PaymentChannel;
import com.delivery.payment.gateways.ExternalPaymentGateway;
import com.delivery.payment.infra.exceptions.IncompatiblePaymentMethodException;
import com.delivery.payment.infra.exceptions.InvalidPaymentChannelException;
import com.delivery.payment.infra.exceptions.InvalidPaymentMethodException;
import com.delivery.payment.infra.exceptions.PaymentAlreadyProcessedException;
import com.delivery.payment.messaging.producer.PaymentProducer;
import com.delivery.payment.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ExternalPaymentGateway externalPaymentGateway;
    private final PaymentProducer paymentProducer;

    public PaymentService(PaymentRepository paymentRepository, ExternalPaymentGateway externalPaymentGateway, PaymentProducer paymentProducer) {
        this.paymentRepository = paymentRepository;
        this.externalPaymentGateway = externalPaymentGateway;
        this.paymentProducer = paymentProducer;
    }

    public void createPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.orderId());
        payment.setPaymentMethod(paymentRequest.paymentMethod());
        payment.setValue(paymentRequest.value());
        if (paymentRequest.paymentChannel() == PaymentChannel.ONLINE) {
            PaymentStatus status = processOnlinePayment(paymentRequest.orderId(), paymentRequest.value(), paymentRequest.paymentMethod(), paymentRequest.cardToken());
            payment.setStatus(status);
        } else {
            payment.setStatus(PaymentStatus.PENDING);
        }
        paymentRepository.save(payment);
    }

    public PaymentStatus processOnlinePayment(Long orderId, BigDecimal value, PaymentMethod paymentMethod, String cardToken) {
        PaymentStatus status = externalPaymentGateway.makePayment(value, paymentMethod, cardToken);
        paymentProducer.sendPaymentStatusMessage(orderId, status == PaymentStatus.APPROVED);
        return status;
    }

    public void processAtDeliveryPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Payment not found for order ID: " + orderId));
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentAlreadyProcessedException("Payment is not in a pending state for order ID: " + orderId);
        }
        payment.setStatus(PaymentStatus.APPROVED);
        paymentRepository.save(payment);
        paymentProducer.sendPaymentStatusMessage(orderId, true);
    }

    public void validatePaymentRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.paymentMethod() == null) {
            throw new InvalidPaymentMethodException("Payment method is required");
        }
        if (paymentRequest.paymentChannel() == null) {
            throw new InvalidPaymentChannelException("Payment channel is required");
        }
        if (paymentRequest.paymentMethod() == PaymentMethod.CASH && paymentRequest.paymentChannel() == PaymentChannel.ONLINE) {
            throw new IncompatiblePaymentMethodException("Cash payments cannot be processed online");
        }
        boolean isCard = paymentRequest.paymentMethod() == PaymentMethod.CREDIT_CARD || paymentRequest.paymentMethod() == PaymentMethod.DEBIT_CARD;
        boolean isOnline = paymentRequest.paymentChannel() == PaymentChannel.ONLINE;
        if (isCard && isOnline) {
            if (paymentRequest.cardToken() == null || paymentRequest.cardToken().isEmpty()) {
                throw new IncompatiblePaymentMethodException("Card data is required for online card payments");
            }
        }
    }
}
