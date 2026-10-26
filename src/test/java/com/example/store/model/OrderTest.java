package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void shouldThrowExceptionForOrderIdNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(null, validCustomer, validItems, validTotalPrice, validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order ID cannot be null");
    }

    @Test
    void shouldThrowExceptionForCustomerNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, null, validItems, validTotalPrice, validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer cannot be null");
    }

    @Test
    void shouldThrowExceptionForItemsNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, null, validTotalPrice, validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order must contain at least one item and cannot be null");
    }

    @Test
    void shouldThrowExceptionForEmptyItemList() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, List.of(), validTotalPrice, validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order must contain at least one item and cannot be null");
    }

    @Test
    void shouldThrowExceptionForPriceNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, null, validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total price must be greater than zero and cannot be null");
    }

    @Test
    void shouldThrowExceptionForNegativePrice() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, BigDecimal.valueOf(-1000), validOrderDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Total price must be greater than zero and cannot be null");
    }

    @Test
    void shouldThrowExceptionForOrderDateNull() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, validTotalPrice, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order date cannot be null or in the future");
    }

    @Test
    void shouldThrowExceptionWhenOrderDateIsInFuture() {
        // Act & Assert
        assertThatThrownBy(() ->
                new Order(validOrderId, validCustomer, validItems, validTotalPrice, LocalDateTime.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order date cannot be null or in the future");
    }
}