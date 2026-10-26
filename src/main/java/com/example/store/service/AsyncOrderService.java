package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.Customer;
import com.example.store.model.Order;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AsyncOrderService {

    private final OrderService orderService;
    private final ExecutorService executorService;

    public AsyncOrderService(OrderService orderService, ExecutorService executorService) {
        this.orderService = orderService;
        this.executorService = executorService;
    }

    public CompletableFuture<Order> processOrderAsync(Customer customer, Cart cart) {
        return CompletableFuture.supplyAsync(
                () -> orderService.createOrder(customer, cart),
                executorService
        );
    }

    public void shutdown() {
        executorService.shutdownNow();
    }
}
