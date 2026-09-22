package com.example.store.model;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)

public class Smartphone extends Product {
    private final Color color;
    private final BatteryCapacity batteryCapacity;
    private final List<String> accessories;

    public Smartphone(UUID id, String name, BigDecimal price, int quantity, Color color, BatteryCapacity batteryCapacity, List<String> accessories) {
        super(id, name, price, quantity);

        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        if (batteryCapacity == null) {
            throw new IllegalArgumentException("Battery capacity cannot be null");
        }

        if (accessories == null) {
            throw new IllegalArgumentException("Accessories cannot be null");
        }

        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = accessories;
    }
}
