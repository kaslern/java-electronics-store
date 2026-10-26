package com.example.store.repo;

import com.example.store.exception.FileStorageException;
import com.example.store.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FileOrderRepository implements OrderRepository {
    private final File file;
    private final Map<UUID, Order> orders;

    public FileOrderRepository(File file) {
        this.file = file;
        this.orders = loadOrdersFromFile(file);
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Order> loadOrdersFromFile(File file) {
        if (!file.exists() || file.length() == 0) {
            log.info("Order file does not exist or is empty. Initialized empty repository. File: {}", file.getName());
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, Order> loaded = (Map<UUID, Order>) ois.readObject();
            log.info("Successfully loaded {} orders from file: {}", loaded.size(), file.getName());
            return loaded;
        } catch (InvalidClassException e) {
            log.warn("Incompatible order serialization format in file {}. Starting with empty repository.", file.getName());
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read orders from file: {}", file.getName(), e);
            throw new FileStorageException("Could not read orders from file: " + file.getName(), e);
        }
    }

    @Override
    public synchronized Order save(Order order) {
        this.orders.put(order.getOrderId(), order);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(orders);
            log.info("Order ID: {} successfully persisted to file: {}", order.getOrderId(), file.getName());
        } catch (IOException e) {
            log.error("Failed to save order ID: {} to file", order.getOrderId(), e);
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