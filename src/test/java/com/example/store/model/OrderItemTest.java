package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderItemTest {

    private Product validProduct;
    private int validQuantity;
    private BigDecimal validPurchasePrice;

    @BeforeEach
    void setUp() {
        validProduct = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("iPhone 18")
                .price(BigDecimal.valueOf(5000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();
        validQuantity = 1;
        validPurchasePrice = BigDecimal.valueOf(5000);
    }

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        assertThatThrownBy(() ->
                new OrderItem(null, validQuantity, validPurchasePrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        assertThatThrownBy(() ->
                new OrderItem(validProduct, 0, validPurchasePrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatThrownBy(() ->
                new OrderItem(validProduct, -1, validPurchasePrice))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenPurchasePriceIsNull() {
        assertThatThrownBy(() ->
                new OrderItem(validProduct, validQuantity, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Purchase price must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenPurchasePriceIsZero() {
        assertThatThrownBy(() ->
                new OrderItem(validProduct, validQuantity, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Purchase price must be greater than zero");
    }

    @Test
    void shouldThrowExceptionWhenPurchasePriceIsNegative() {
        assertThatThrownBy(() ->
                new OrderItem(validProduct, validQuantity, BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Purchase price must be greater than zero");
    }
}