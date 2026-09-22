package com.example.store.model;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@ToString(callSuper = true)
@Getter

public class Computer extends Product {
    private final ProcessorType processorType;
    private final RamSize ramSize;

    public Computer(UUID id, String name, BigDecimal price, int quantity, ProcessorType processorType, RamSize ramSize) {
        super(id, name, price, quantity);

        if (processorType == null) {
            throw new IllegalArgumentException("Processor type cannot be null");
        }

        if (ramSize == null) {
            throw new IllegalArgumentException("Ram size cannot be null");
        }

        this.processorType = processorType;
        this.ramSize = ramSize;
    }
}
