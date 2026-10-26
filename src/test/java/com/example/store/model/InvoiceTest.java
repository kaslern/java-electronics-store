package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

        smartphone1 = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("Iphone 18")
                .price(BigDecimal.valueOf(5000))
                .quantity(1)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();

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