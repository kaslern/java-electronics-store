package com.example.store.service;

import com.example.store.model.Cart;
import com.example.store.model.Product;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CartManager {
    private final ProductManager productManager;

    public CartManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void addProductToCart(UUID productId, Cart cart) {
        if (productId == null || cart == null) {
            log.warn("Invalid arguments for adding product to cart: cart or productId is null");
            throw new IllegalArgumentException("Cart and product ID must not be null");
        }

        Product product = productManager.findById(productId);
        cart.addProduct(product);
        log.info("Product {} added to cart {}", product.getName(), cart.getId());
    }

    public void removeProductFromCart(UUID productId, Cart cart) {
        if (productId == null || cart == null) {
            log.warn("Invalid arguments for removing product from cart");
            throw new IllegalArgumentException("Cart and product ID must not be null");
        }

        boolean removed = cart.removeProduct(productId);
        if (!removed) {
            log.warn("Product with ID {} not found in cart {}", productId, cart.getId());
            throw new IllegalArgumentException("Product not found in cart");
        }
        log.info("Product with ID {} removed from cart {}", productId, cart.getId());
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