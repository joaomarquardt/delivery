package com.api.delivery.services;

import com.api.delivery.domain.Order;
import com.api.delivery.domain.OrderItem;
import com.api.delivery.enums.OrderStatus;
import com.api.delivery.domain.Product;
import com.api.delivery.dtos.requests.OrderItemRequest;
import com.api.delivery.dtos.requests.CreateOrderRequest;
import com.api.delivery.enums.PaymentChannel;
import com.api.delivery.infra.exceptions.InvalidOrderStatusTransitionException;
import com.api.delivery.infra.exceptions.InvalidPaymentDetailsException;
import com.api.delivery.messaging.dtos.OrderPaymentMessage;
import com.api.delivery.dtos.requests.UpdateOrderStatusRequest;
import com.api.delivery.dtos.responses.OrderResponse;
import com.api.delivery.mappers.OrderMapper;
import com.api.delivery.messaging.producers.OrderProducer;
import com.api.delivery.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
    private final OrderProducer orderProducer;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductService productService, OrderProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.orderProducer = orderProducer;
    }


    public List<OrderResponse> findAll(Long userId, String userRole) {
        List<Order> orders;
        if (userRole.equals("ADMIN")) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findAllByUserId(userId);
        }
        return orderMapper.toOrderResponseList(orders);
    }

    public Order findOrderEntityById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
    }

    public OrderResponse findOrderById(Long id, Long userId, String userRole) {
        Order order = findOrderEntityById(id);
        if (!userRole.equals("ADMIN") && !order.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this order");
        }
        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        if (request.paymentChannel() == PaymentChannel.ONLINE && (request.cardToken() == null || request.cardToken().isBlank())) {
            throw new InvalidPaymentDetailsException("Card token is required for online card payments");
        }
        BigDecimal totalValue = BigDecimal.valueOf(0);
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.findProductEntityById(itemRequest.productId());
            if (product == null) {
                throw new EntityNotFoundException("Product not found with ID: " + itemRequest.productId());
            }
            BigDecimal totalItemValue = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.amount()));
            totalValue = totalValue.add(totalItemValue);
            OrderItem orderItem = new OrderItem();
            orderItem.setAmount(itemRequest.amount());
            orderItem.setProduct(product);
            orderItem.setPrice(product.getPrice());
            items.add(orderItem);
        }
        Order order = new Order();
        order.setTotalValue(totalValue);
        order.setStatus(OrderStatus.PENDING);
        order.setUserId(userId);
        order.setOrderedOn(Timestamp.from(Instant.now()));
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        Order createdOrder = orderRepository.save(order);

        OrderPaymentMessage paymentMessage = new OrderPaymentMessage(
                createdOrder.getId(),
                request.paymentMethod(),
                request.paymentChannel(),
                totalValue,
                request.cardToken()
        );
        orderProducer.sendOrderCreatedMessage(paymentMessage);
        return orderMapper.toOrderResponse(createdOrder);
    }

    public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = findOrderEntityById(id);
        validateOrderStatusUpdate(order.getStatus(), request.orderStatus());
        order.setStatus(request.orderStatus());
        Order createdOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(createdOrder);
    }

    private void validateOrderStatusUpdate(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == newStatus) return;
        if (currentStatus == OrderStatus.DELIVERED || currentStatus == OrderStatus.CANCELED) {
            throw new InvalidOrderStatusTransitionException("Cannot change status of a delivered or canceled order.");
        }
        if (currentStatus == OrderStatus.PENDING && newStatus != OrderStatus.PREPARING && newStatus != OrderStatus.CANCELED) {
            throw new InvalidOrderStatusTransitionException("Pending orders can only transition to CANCELED, or PREPARING.");
        }
        if (currentStatus == OrderStatus.PAID && newStatus != OrderStatus.PREPARING && newStatus != OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusTransitionException("Paid orders must transition to PREPARING or DELIVERED.");
        }
        if (currentStatus == OrderStatus.PREPARING && newStatus != OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStatusTransitionException("Orders in preparation must transition to OUT_FOR_DELIVERY.");
        }
        if (currentStatus == OrderStatus.OUT_FOR_DELIVERY && newStatus != OrderStatus.DELIVERED && newStatus != OrderStatus.PAID) {
            throw new InvalidOrderStatusTransitionException("Orders out for delivery must transition to PAID or DELIVERED.");
        }

    }
}
