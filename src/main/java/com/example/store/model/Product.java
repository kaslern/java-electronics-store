package com.example.store.model;

import com.example.store.exception.InsufficientStockException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "id")
@ToString

public abstract class Product implements Serializable {
    private final UUID id;
    private final String manufacturer;
    private final String model;
    private BigDecimal price;
    private int quantity;

    public Product(UUID id, String manufacturer, String model, BigDecimal price, int quantity) {
        validate(id, manufacturer, model, price, quantity);
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.price = price;
        this.quantity = quantity;
    }

    private void validate(UUID id, String manufacturer, String model, BigDecimal price, int quantity) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (manufacturer == null || manufacturer.isBlank()) {
            throw new IllegalArgumentException("Manufacturer cannot be null or empty");
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be null or below zero");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public String getName() {
        return manufacturer + " " + model;
    }

    public void decreaseQuantity(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (this.quantity == 0) {
            throw new InsufficientStockException("Product out of stock");
        }
        if (amount > this.quantity) {
            throw new InsufficientStockException("Not enough stock available! Available pcs: " + quantity);
        }
        this.quantity -= amount;
    }
}