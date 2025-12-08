package com.api.delivery.services;

import com.api.delivery.domain.Order;
import com.api.delivery.domain.OrderItem;
import com.api.delivery.enums.OrderStatus;
import com.api.delivery.domain.Product;
import com.api.delivery.dtos.requests.OrderItemRequest;
import com.api.delivery.dtos.requests.CreateOrderRequest;
import com.api.delivery.dtos.requests.OrderPaymentMessage;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.dtos.responses.OrderResponse;
import com.api.delivery.mappers.OrderMapper;
import com.api.delivery.messaging.producers.OrderProducer;
import com.api.delivery.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductService productService, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.orderProducer = orderProducer;
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderResponseList(orders);
    }

    public Order findOrderEntityById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    public OrderResponse findOrderById(Long id) {
        Order order = findOrderEntityById(id);
        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        BigDecimal totalValue = BigDecimal.valueOf(0);
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.findProductEntityById(itemRequest.productId());
            totalValue = totalValue.add(product.getPrice());
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(itemRequest.amount());
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            items.add(orderItem);
        }
        Order order = new Order();
        order.setTotalValue(totalValue);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedOn(Timestamp.from(Instant.now()));
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        Order createdOrder = orderRepository.save(order);

        OrderPaymentMessage paymentMessage = new OrderPaymentMessage(
                createdOrder.getId(),
                request.paymentMethod(),
                totalValue,
                request.cardToken()
        );
        orderProducer.sendOrderCreatedMessage(paymentMessage);
        return orderMapper.toOrderResponse(createdOrder);
    }

    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = findOrderEntityById(id);
        order.setStatus(request.orderStatus());
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(createdOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
