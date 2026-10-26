package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartManager {
    private final ProductManager productManager;

    public CartManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void addProductToCart(UUID productId, Cart cart) {
        if (productId == null || cart == null) {
            throw new IllegalArgumentException("Cart and product ID must not be null");
        }

        Product product = productManager.findById(productId);
        cart.addProduct(product);
    }

    public void removeProductFromCart(UUID productId, Cart cart) {
        if (productId == null || cart == null) {
            throw new IllegalArgumentException("Cart and product ID must not be null");
        }

        boolean removed = cart.removeProduct(productId);
        if (!removed) {
            throw new IllegalArgumentException("Product not found in cart");
        }
    }

    public List<Product> getAllProductsFromCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null");
        }
        return cart.getProducts();
    }

    public BigDecimal calculateTotalPrice(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null");
        }
        return cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}