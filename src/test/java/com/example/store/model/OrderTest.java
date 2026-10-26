package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    private Address validAddress;
    private UUID validOrderId;
    private Customer validCustomer;
    private List<OrderItem> validItems;
    private OrderItem validOrderItem;
    private BigDecimal validTotalPrice;
    private LocalDateTime validOrderDate;
    private Smartphone smartphone1;
    private Clock fixedClock;


    @BeforeEach
    void setUp() {
        validAddress = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");

        validCustomer = new Customer(UUID.randomUUID(), "Janusz",
                "Nosacz", "kiedysToBylo@prl.ru", validAddress);

        Instant fixedInstant = Instant.parse("2026-10-26T10:00:00Z");
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        fixedClock = Clock.fixed(fixedInstant, zoneId);

        smartphone1 = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("iPhone 18")
                .price(BigDecimal.valueOf(5000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();

        validOrderItem = new OrderItem(smartphone1, 1, BigDecimal.valueOf(5000));
        validItems = List.of(validOrderItem);

        validOrderId = UUID.randomUUID();
        validTotalPrice = BigDecimal.valueOf(5000);
        validOrderDate = LocalDateTime.now();
    }

    @Test
    void shouldThrowExceptionForOrderIdNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(null, validCustomer, validItems, validTotalPrice, validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order ID cannot be null");
    }

    @Test
    void shouldThrowExceptionForCustomerNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, null, validItems, validTotalPrice, validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer cannot be null");
    }

    @Test
    void shouldThrowExceptionForItemsNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, null, validTotalPrice, validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order must contain at least one item and cannot be null");
    }

    @Test
    void shouldThrowExceptionForEmptyItemList() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, List.of(), validTotalPrice, validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order must contain at least one item and cannot be null");
    }

    @Test
    void shouldThrowExceptionForPriceNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, null, validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total price must be greater than zero and cannot be null");
    }

    @Test
    void shouldThrowExceptionForNegativePrice() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, BigDecimal.valueOf(-1000), validOrderDate, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total price must be greater than zero and cannot be null");
    }

    @Test
    void shouldThrowExceptionForOrderDateNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, validTotalPrice, null, fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order date cannot be null or in the future");
    }

    @Test
    void shouldThrowExceptionWhenOrderDateIsInFuture() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, validTotalPrice, LocalDateTime.now().plusDays(1), fixedClock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order date cannot be null or in the future");
    }
}