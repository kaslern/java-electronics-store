package com.example.store.service;

import com.example.store.model.Product;
import com.example.store.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ProductManager {
    private final ProductRepository productRepository;

    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        if (product == null) {
            log.warn("Attempted to add null product");
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (productRepository.findById(product.getId()).isPresent()) {
            log.warn("Product already exists with ID: {}", product.getId());
            throw new IllegalArgumentException("Product already exists");
        }
        productRepository.save(product);
        log.info("Product added successfully: {} (ID: {})", product.getName(), product.getId());
    }

    public void removeProduct(UUID id) {
        if (id == null) {
            log.warn("Attempted to remove product with null ID");
            throw new IllegalArgumentException("Id cannot be null");
        }
        productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for removal, ID: {}", id);
                    return new IllegalArgumentException("Product not exist");
                });
        productRepository.deleteById(id);
        log.info("Product removed with ID: {}", id);
    }

    public void update(Product product) {
        if (product == null) {
            log.warn("Attempted to update null product");
            throw new IllegalArgumentException("Product cannot be null");
        }
        productRepository.findById(product.getId())
                .orElseThrow(() -> {
                    log.warn("Product not found for update, ID: {}", product.getId());
                    return new IllegalArgumentException("Product not exist");
                });
        productRepository.save(product);
        log.info("Product updated successfully: ID: {}", product.getId());
    }

    public Product findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not exist"));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}