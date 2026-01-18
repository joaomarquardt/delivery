package com.api.delivery.domain;

import com.api.delivery.enums.OrderStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp orderedOn;
    @LastModifiedDate
    private Timestamp updatedOn;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal totalValue;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @JoinColumn(name = "user_id")
    private Long userId;

    public Order() {
    }

    public Order(Long id, Timestamp orderedOn, OrderStatus status, BigDecimal totalValue, List<OrderItem> items, Long userId) {
        this.id = id;
        this.orderedOn = orderedOn;
        this.status = status;
        this.totalValue = totalValue;
        this.items = items;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
