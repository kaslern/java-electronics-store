package com.example.store.service;

import com.example.store.model.*;
import com.example.store.repo.OrderRepository;
import com.example.store.service.discount.DiscountService;
import com.example.store.validation.ApprovalChain;
import com.example.store.validation.OrderContext;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public class OrderService {
    private final CartManager cartManager;
    private final OrderRepository orderRepository;
    private final DiscountService discountService;
    private final ApprovalChain approvalChain;
    private final Clock clock;

    public OrderService(CartManager cartManager,
                        OrderRepository orderRepository,
                        DiscountService discountService,
                        ApprovalChain approvalChain,
                        Clock clock) {
        this.cartManager = cartManager;
        this.orderRepository = orderRepository;
        this.discountService = discountService;
        this.approvalChain = approvalChain;
        this.clock = clock;
    }

    public Order createOrder(Customer customer, Cart cart) {
        log.info("Starting order creation process");

        OrderContext context = new OrderContext(customer, cart);
        approvalChain.validate(context).ifPresent(rejection -> {
            log.warn("Order validation failed: {}", rejection.reason());
            throw new IllegalStateException(rejection.reason());
        });

        for (Product product : cart.getProducts()) {
            product.decreaseQuantity(1);
        }

        BigDecimal basePrice = cartManager.calculateTotalPrice(cart);
        LocalDateTime orderDate = LocalDateTime.now(clock);
        BigDecimal finalPrice = discountService.applyDiscount(basePrice, orderDate, cart.getPromoCode());

        List<OrderItem> items = cart.getProducts().stream()
                .map(product -> new OrderItem(product, 1, product.getPrice()))
                .toList();

        Order order = new Order(UUID.randomUUID(), customer, items, finalPrice, orderDate, clock);

        simulateLongOrderProcessing();

        orderRepository.save(order);
        cart.clear();

        log.info("Order created successfully with ID: {}", order.getOrderId());
        return order;
    }

    private void simulateLongOrderProcessing() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Order processing thread was interrupted", e);
            throw new IllegalStateException("Processing has stopped", e);
        }
    }
}