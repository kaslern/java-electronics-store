package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.Customer;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;
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

        BigDecimal cartTotalPrice = cartManager.calculateTotalPrice(cart);
        List<OrderItem> items = cart.getProducts().stream()
                .map(product -> new OrderItem(product, product.getQuantity(), product.getPrice()))
                .toList();
        Order order = new Order(UUID.randomUUID(), customer, items, cartTotalPrice, LocalDateTime.now());
        orderRepository.save(order);

        cart.clear();
        return order;
    }
}
