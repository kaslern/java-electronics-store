package com.example.store.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record Invoice(
        String invoiceNumber,
        LocalDate issueDate,
        Order order
) {
    public Invoice {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new IllegalArgumentException("Invoice number cannot be null or empty");
        }
        if (issueDate == null) {
            throw new IllegalArgumentException("Issue date cannot be null");
        }
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }

    @Override
    public String toString() {
        String template = """
                ==================================
                INVOICE NO: %s
                Order Date: %s
                Customer: %s %s
                ----------------------------------
                ITEMS:
                %s
                ----------------------------------
                Total Amount: %s PLN
                ==================================
                """;

        return template.formatted(
                invoiceNumber,
                order.getOrderDate(),
                order.getCustomer().getFirstName(),
                order.getCustomer().getLastName(),
                formatOrderItems(order.getItems()),
                order.getTotalPrice()
        );
    }

    private String formatOrderItems(List<OrderItem> items) {
        return items.stream()
                .map(item -> "%s - %d pcs x %s PLN".formatted(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPurchasePrice()
                ))
                .collect(Collectors.joining("\n"));
    }
}