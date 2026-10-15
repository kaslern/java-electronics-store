package com.example.store.validation;

import com.example.store.model.Product;
import java.util.Optional;

public class StockAvailabilityStep implements ApprovalStep {

    @Override
    public Optional<Rejection> check(OrderContext context) {
        for (Product product : context.cart().getProducts()) {
            if (product.getQuantity() < 1) {
                return Optional.of(new Rejection("stock", "Product out of stock: " + product.getName()));
            }
        }
        return Optional.empty();
    }
}