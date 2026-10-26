package com.example.store.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record Invoice(
        String invoiceNumber,
        LocalDate issueDate,
        Order order
) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        BigDecimal subtotal = order.getItems().stream()
                .map(item -> item.getPurchasePrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = subtotal.subtract(order.getTotalPrice());
        String formattedOrderDate = order.getOrderDate().format(DATE_FORMATTER);

        String template = """
                ==================================
                INVOICE NO: %s
                Order Date: %s
                Customer: %s %s
                ----------------------------------
                ITEMS:
                %s
                ----------------------------------
                Subtotal: %s PLN
                Discount: -%s PLN
                Total Amount: %s PLN
                ==================================
                """;

        return template.formatted(
                invoiceNumber,
                formattedOrderDate,
                order.getCustomer().getFirstName(),
                order.getCustomer().getLastName(),
                formatOrderItems(order.getItems()),
                subtotal,
                discount,
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