package com.api.delivery.services;

import com.api.delivery.domain.Order;
import com.api.delivery.dtos.requests.CreateOrderRequest;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.dtos.responses.OrderResponse;
import com.api.delivery.mappers.OrderMapper;
import com.api.delivery.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
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
        Order order = orderMapper.toOrderEntity(request);
        Order createdOrder = orderRepository.save(order);
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
