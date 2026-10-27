package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.Customer;
import com.example.store.model.Order;

public class SyncOrderService {

    private final OrderService orderService;

    public SyncOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order processOrderSync(Customer customer, Cart cart) {
        return orderService.createOrder(customer, cart);
    }
}