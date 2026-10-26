package com.example.store.model;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Product product;
    private final int quantity;
    private final BigDecimal purchasePrice;

    public OrderItem(Product product, int quantity, BigDecimal purchasePrice) {
        validate(product, quantity, purchasePrice);
        this.product = product;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }

    private void validate(Product product, int quantity, BigDecimal purchasePrice) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Purchase price must be greater than zero");
        }
    }
}