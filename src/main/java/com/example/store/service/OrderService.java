package com.example.store.service;

import com.example.store.exception.InsufficientStockException;
import com.example.store.model.*;
import com.example.store.repo.OrderRepository;
import com.example.store.service.discount.DiscountService;
import com.example.store.validation.OrderContext;
import com.example.store.validation.ApprovalChain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final CartManager cartManager;
    private final OrderRepository orderRepository;
    private final DiscountService discountService;
    private final ApprovalChain approvalChain;

    public OrderService(CartManager cartManager,
                        OrderRepository orderRepository,
                        DiscountService discountService,
                        ApprovalChain approvalChain) {
        this.cartManager = cartManager;
        this.orderRepository = orderRepository;
        this.discountService = discountService;
        this.approvalChain = approvalChain;
    }

    public Order createOrder(Customer customer, Cart cart) {

        OrderContext context = new OrderContext(customer, cart);
        approvalChain.validate(context).ifPresent(rejection -> {
            throw new IllegalStateException(rejection.reason());
        });

        for (Product product : cart.getProducts()) {
            product.decreaseQuantity(1);
        }

        BigDecimal basePrice = cartManager.calculateTotalPrice(cart);
        LocalDateTime orderDate = LocalDateTime.now();
        BigDecimal finalPrice = discountService.applyDiscount(basePrice, orderDate, cart.getPromoCode());
        List<OrderItem> items = cart.getProducts().stream()
                .map(product -> new OrderItem(product, 1, product.getPrice()))
                .toList();
        Order order = new Order(UUID.randomUUID(), customer, items, finalPrice, orderDate);
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
