package com.delivery.payment.gateways;

import com.delivery.payment.domain.PaymentMethod;
import com.delivery.payment.domain.PaymentStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class ExternalPaymentGateway {
    private final RestClient restClient;

    public ExternalPaymentGateway(@Qualifier("genericClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Método criado para simular a resposta de um gateway de pagamento externo
    public PaymentStatus makePayment(BigDecimal value, PaymentMethod paymentMethod, String cardToken) {
        Double randomValue = Math.random();
        return (randomValue < 0.8) ? PaymentStatus.APPROVED : PaymentStatus.REFUSED;
    }
}
