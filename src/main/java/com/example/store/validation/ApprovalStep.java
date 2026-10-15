package com.example.store.validation;

import java.util.Optional;

@FunctionalInterface
interface ApprovalStep {
    Optional<Rejection> check(OrderContext context);
}

