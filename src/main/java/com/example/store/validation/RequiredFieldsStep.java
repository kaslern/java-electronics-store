package com.example.store.validation;

import java.util.Optional;

public final class RequiredFieldsStep implements ApprovalStep {
    @Override
    public Optional<Rejection> check(OrderContext context) {
        if (context.customer() == null || context.cart() == null) {
            return Optional.of(new Rejection("required fields", "customer or cart cannot be null"));
        }
        if (context.cart().getProducts().isEmpty()) {
            return Optional.of(new Rejection("required fields", "cart is empty"));
        }
        return Optional.empty();
    }
}
