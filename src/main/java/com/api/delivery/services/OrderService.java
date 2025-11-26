package com.api.delivery.services;

import com.api.delivery.domain.Order;
import com.api.delivery.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    public Order createOrder(Order order) {
        Order createdOrder = orderRepository.save(order);
        return createdOrder;
    }

    public Order updateOrder(Long id, Order order) {
        Order createdOrder = orderRepository.save(order);
        return createdOrder;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
