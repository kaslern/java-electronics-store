package com.example.store.validation;

import java.util.List;
import java.util.Optional;

public class ApprovalChain {
    private final List<ApprovalStep> steps;

    public ApprovalChain(List<ApprovalStep> steps) {
        this.steps = steps;
    }

    public Optional<Rejection> validate(OrderContext context) {
        for (ApprovalStep step : steps) {
            Optional<Rejection> rejection = step.check(context);
            if (rejection.isPresent()) {
                return rejection;
            }
        }
        return Optional.empty();
    }
}