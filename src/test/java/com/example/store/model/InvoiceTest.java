package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {
    private Address validAddress;
    private UUID validOrderId;
    private Customer validCustomer;
    private List<OrderItem> validItems;
    private OrderItem validOrderItem;
    private BigDecimal validTotalPrice;
    private LocalDateTime validOrderDate;
    private Smartphone smartphone1;
    private Order validOrder;
    private String validInvoiceNumber;
    private LocalDate validIssueDate;


    @BeforeEach
    void setUp() {
        validAddress = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");

        validCustomer = new Customer(UUID.randomUUID(), "Janusz",
                "Nosacz", "kiedysToBylo@prl.ru", validAddress);

        List<String> accessories = List.of("Charger");
        smartphone1 = new Smartphone(UUID.randomUUID(), "Apple",
                "Iphone 18", BigDecimal.valueOf(5000), 1,
                Color.BLACK, BatteryCapacity.BATTERY_4500MAH, accessories);

        validOrderItem = new OrderItem(smartphone1, 1, BigDecimal.valueOf(5000));
        validItems = List.of(validOrderItem);

        validOrderId = UUID.randomUUID();
        validTotalPrice = BigDecimal.valueOf(5000);
        validOrderDate = LocalDateTime.now();

        validOrder = new Order(validOrderId, validCustomer, validItems, validTotalPrice, validOrderDate);
        validInvoiceNumber = "INV-2026-0001";
        validIssueDate = LocalDate.now();
    }

    @Test
    void shouldFormatToStringCorrectly() {
        // Arrange
        Invoice invoice = new Invoice(validInvoiceNumber, validIssueDate, validOrder);

        // Act
        String result = invoice.toString();

        // Assert
        assertThat(result)
                .contains("INVOICE NO: INV-2026-0001")
                .contains("Customer: Janusz Nosacz")
                .contains("Iphone 18 - 1 pcs x 5000 PLN")
                .contains("Total Amount: 5000 PLN");
    }

}