package com.example.store.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)

public class Smartphone extends Product {
    private final Color color;
    private final BatteryCapacity batteryCapacity;
    private final List<String> accessories;

    @Builder
    public Smartphone(UUID id, String manufacturer, String model, BigDecimal price, int quantity,
                      Color color, BatteryCapacity batteryCapacity, List<String> accessories) {
        super(id, manufacturer, model, price, quantity);
        validate(color, batteryCapacity, accessories);
        this.color = color;
        this.batteryCapacity = batteryCapacity;
        this.accessories = List.copyOf(accessories);
    }

    private void validate(Color color, BatteryCapacity batteryCapacity, List<String> accessories) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (batteryCapacity == null) {
            throw new IllegalArgumentException("Battery capacity cannot be null");
        }
        if (accessories == null) {
            throw new IllegalArgumentException("Accessories cannot be null");
        }
    }
}