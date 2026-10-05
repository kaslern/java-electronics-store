package com.example.store.repo;

import com.example.store.model.Order;

import java.util.*;

public class InMemoryOrderRepository implements OrderRepository{
    private final Map<UUID, Order> orders = new HashMap<>();

    @Override
    public Order save(Order order){
        orders.put(order.getOrderId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }
}
