package com.api.delivery.controllers;

import com.api.delivery.dtos.requests.CreateOrderRequest;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.dtos.responses.OrderResponse;
import com.api.delivery.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(@RequestHeader("X-User-Id") Long userId, @RequestHeader("X-User-Role") String userRole) {
        List<OrderResponse> orders = orderService.findAll(userId, userRole);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable(value = "id") Long id, @RequestHeader("X-User-Id") Long userId, @RequestHeader("X-User-Role") String userRole) {
        OrderResponse order = orderService.findOrderById(id, userId, userRole);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest order, @RequestHeader("X-User-Id") Long userId) {
        OrderResponse createdOrder = orderService.createOrder(order, userId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable(value = "id") Long id, @RequestBody UpdateOrderStatusRequest orderStatus) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, orderStatus);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
