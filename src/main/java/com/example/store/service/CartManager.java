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

    public void addProductToCart(Product product, Cart cart) {
        validateNotNull(product, "Product must not be null");
        validateCart(cart);

        cart.addProduct(product);
        log.info("Product {} added to cart {}", product.getName(), cart.getId());
    }

    public void removeProductFromCart(UUID productId, Cart cart) {
        validateNotNull(productId, "Product ID must not be null");
        validateCart(cart);

        boolean removed = cart.removeProduct(productId);
        if (!removed) {
            log.warn("Product with ID {} not found in cart {}", productId, cart.getId());
            throw new IllegalArgumentException("Product not found in cart");
        }
        log.info("Product with ID {} removed from cart {}", productId, cart.getId());
    }

    public List<Product> getAllProductsFromCart(Cart cart) {
        validateCart(cart);
        return cart.getProducts();
    }

    public BigDecimal calculateTotalPrice(Cart cart) {
        validateCart(cart);
        return cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateCart(Cart cart) {
        if (cart == null) {
            log.warn("Cart argument is null");
            throw new IllegalArgumentException("Cart must not be null");
        }
    }

    private void validateNotNull(Object object, String message) {
        if (object == null) {
            log.warn("Validation failed: {}", message);
            throw new IllegalArgumentException(message);
        }
    }
}