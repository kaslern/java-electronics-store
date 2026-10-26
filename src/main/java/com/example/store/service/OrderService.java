package com.example.store.service;

import com.example.store.exception.InsufficientStockException;
import com.example.store.model.*;
import com.example.store.repo.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final CartManager cartManager;
    private final OrderRepository orderRepository;

    public OrderService(CartManager cartManager, OrderRepository orderRepository) {
        this.cartManager = cartManager;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Customer customer, Cart cart) {
        if (customer == null || cart == null) {
            throw new IllegalArgumentException("Customer or cart must not be null");
        }
        if (cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        for (Product product : cart.getProducts()) {
            if (product.getQuantity() < 1) {
                throw new InsufficientStockException("Product out of stock: " + product.getName());
            }
        }

        for (Product product : cart.getProducts()) {
            product.decreaseQuantity(1);
        }

        BigDecimal cartTotalPrice = cartManager.calculateTotalPrice(cart);
        List<OrderItem> items = cart.getProducts().stream()
                .map(product -> new OrderItem(product, 1, product.getPrice()))
                .toList();
        Order order = new Order(UUID.randomUUID(), customer, items, cartTotalPrice, LocalDateTime.now());
        orderRepository.save(order);

        simulateLongOrderProcessing();

        cart.clear();
        return order;
    }

    private void simulateLongOrderProcessing() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Processing has stopped", e);
        }
    }
}
