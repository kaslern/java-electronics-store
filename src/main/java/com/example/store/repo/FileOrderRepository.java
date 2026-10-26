package com.example.store.repo;

import com.example.store.exception.FileStorageException;
import com.example.store.model.Order;

import java.io.*;
import java.util.*;

public class FileOrderRepository implements OrderRepository {
    private final File file;
    private final Map<UUID, Order> orders;

    public FileOrderRepository(File file) {
        this.file = file;

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                this.orders = (Map<UUID, Order>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new FileStorageException("Could not read orders from file: " + file.getName(), e);
            }
        } else {
            this.orders = new HashMap<>();
        }
    }

    @Override
    public synchronized Order save(Order order) {
        this.orders.put(order.getOrderId(), order);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            throw new FileStorageException("Could not save orders to file", e);
        }
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