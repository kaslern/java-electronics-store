package com.example.store.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Cart {
    private final UUID id;
    private final List<Product> products;

    public Cart() {
        this.id = UUID.randomUUID();
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
        }
    }

    public boolean removeProduct(UUID productId) {
        if (productId == null) {
            return false;
        }
        return this.products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .map(products::remove)
                .orElse(false);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void clear() {
        this.products.clear();
    }
}