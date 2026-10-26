package com.example.store.model;


import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

public class Electronics extends Product {

    @Builder
    public Electronics(UUID id, String manufacturer, String model, BigDecimal price, int quantity) {
        super(id, manufacturer, model, price, quantity);
    }
}


