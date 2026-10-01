package com.example.store.service;

import com.example.store.model.Invoice;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceGenerator {
    private int counter = 1;
    private LocalDate lastResetDate = LocalDate.now();

    public Invoice generateInvoice(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (!lastResetDate.equals(LocalDate.now())) {
            counter = 1;
            lastResetDate = LocalDate.now();
        }
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String invoiceNumber = "INV-%s-%04d".formatted(dateStr, counter++);

        Invoice invoice = new Invoice(invoiceNumber, lastResetDate, order);

        return invoice;
    }


}
