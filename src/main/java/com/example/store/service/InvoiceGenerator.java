package com.example.store.service;

import com.example.store.model.Invoice;
import com.example.store.model.Order;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class InvoiceGenerator {
    private final Clock clock;
    private int counter = 1;
    private LocalDate lastResetDate;

    public InvoiceGenerator(Clock clock) {
        this.clock = clock;
        this.lastResetDate = LocalDate.now(clock);
    }

    public synchronized Invoice generateInvoice(Order order) {
        if (order == null) {
            log.warn("Attempted to generate invoice for null order");
            throw new IllegalArgumentException("Order must not be null");
        }

        LocalDate today = LocalDate.now(clock);

        if (!today.equals(lastResetDate)) {
            counter = 1;
            lastResetDate = today;
        }

        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String invoiceNumber = "INV-%s-%04d".formatted(dateStr, counter++);

        log.info("Generated invoice {} for order ID: {}", invoiceNumber, order.getOrderId());

        return new Invoice(invoiceNumber, today, order);
    }
}