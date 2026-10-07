package com.example.store.service;

import com.example.store.model.Invoice;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InvoiceGenerator {
    private final AtomicInteger counter = new AtomicInteger(1);
    private volatile LocalDate lastResetDate = LocalDate.now();

    public synchronized Invoice generateInvoice(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (!lastResetDate.equals(LocalDate.now())) {
            counter.set(1);
            lastResetDate = LocalDate.now();
        }
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String invoiceNumber = "INV-%s-%04d".formatted(dateStr, counter.getAndIncrement());

        return new Invoice(invoiceNumber, lastResetDate, order);
    }


}
