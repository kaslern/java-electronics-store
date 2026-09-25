package com.example.store.service;

import com.example.store.model.Product;
import com.example.store.repo.ProductRepository;

import java.util.List;
import java.util.UUID;

public class ProductManager {
    private final ProductRepository productRepository;

    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (productRepository.findById(product.getId()).isPresent()) {
            throw new IllegalArgumentException("Product already exists");
        }
        productRepository.save(product);
    }

    public void removeProduct(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not exist"));
        productRepository.deleteById(id);
    }

    public void update(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not exist"));
        productRepository.save(product);
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
