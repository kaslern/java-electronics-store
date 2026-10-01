package com.example.store.repo;

import com.example.store.model.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
}
