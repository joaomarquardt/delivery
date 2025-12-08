package com.api.delivery.domain;

import com.api.delivery.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp orderedOn;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal totalValue;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {
    }

    public Order(Long id, Timestamp orderedOn, OrderStatus status, BigDecimal totalValue, List<OrderItem> items, User user) {
        this.id = id;
        this.orderedOn = orderedOn;
        this.status = status;
        this.totalValue = totalValue;
        this.items = items;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(Timestamp orderedOn) {
        this.orderedOn = orderedOn;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
