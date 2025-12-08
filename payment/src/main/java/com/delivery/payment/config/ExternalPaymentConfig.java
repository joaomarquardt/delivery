package com.delivery.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalPaymentConfig {
    @Bean
    public RestClient genericClient() {
        return RestClient.builder()
                .baseUrl("")
                .build();
    }
}
