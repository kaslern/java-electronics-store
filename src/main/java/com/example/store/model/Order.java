package com.example.store.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID orderId;
    private final Customer customer;
    private final List<OrderItem> items;
    private final BigDecimal totalPrice;
    private final LocalDateTime orderDate;

    public Order(UUID orderId, Customer customer, List<OrderItem> items, BigDecimal totalPrice, LocalDateTime orderDate, Clock clock) {
        validate(orderId, customer, items, totalPrice, orderDate, clock);
        this.orderId = orderId;
        this.customer = customer;
        this.items = List.copyOf(items);
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    private void validate(UUID orderId, Customer customer, List<OrderItem> items, BigDecimal totalPrice, LocalDateTime orderDate, Clock clock) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item and cannot be null");
        }
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price cannot be null or negative");
        }
        if (orderDate == null || orderDate.isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Order date cannot be null or in the future");
        }
    }
}