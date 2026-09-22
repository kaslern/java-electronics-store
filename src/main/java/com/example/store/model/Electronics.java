package com.example.store.model;


import java.math.BigDecimal;
import java.util.UUID;

public class Electronics extends Product {
    public Electronics(UUID id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
    }
}


