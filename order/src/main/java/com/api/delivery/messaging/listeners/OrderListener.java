package com.api.delivery.messaging.listeners;

import com.api.delivery.config.RabbitMQConfig;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.enums.OrderStatus;
import com.api.delivery.messaging.dtos.PaymentStatusMessage;
import com.api.delivery.services.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    private final OrderService orderService;

    public OrderListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_STATUS_QUEUE)
    public void handleOrderStatusUpdate(PaymentStatusMessage message) {
        OrderStatus orderStatus = message.success() ? OrderStatus.PREPARING : OrderStatus.CANCELED;
        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(orderStatus);
        orderService.updateOrderStatus(message.orderId(), request);
    }
}
