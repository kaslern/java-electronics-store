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
    private final DiskSize diskSize;

    public Computer(UUID id, String manufacturer, String model, BigDecimal price, int quantity,
                    ProcessorType processorType, DiskSize diskSize, RamSize ramSize) {

        super(id, manufacturer, model, price, quantity);

        if (processorType == null) {
            throw new IllegalArgumentException("Processor type cannot be null");
        }

        if (diskSize == null) {
            throw new IllegalArgumentException("Disk size cannot be null");
        }

        if (ramSize == null) {
            throw new IllegalArgumentException("Ram size cannot be null");
        }

        this.processorType = processorType;
        this.diskSize = diskSize;
        this.ramSize = ramSize;
    }
}
