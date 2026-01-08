package com.delivery.payment.controller;

import com.delivery.payment.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("{id}/at-delivery")
    public ResponseEntity<Void> processAtDeliveryPayment(@PathVariable(value = "id") Long orderId) {
        paymentService.processAtDeliveryPayment(orderId);
        return ResponseEntity.ok().build();
    }
}
