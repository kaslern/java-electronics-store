package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ElectronicsTest {

    private static final UUID DEFAULT_ID = UUID.randomUUID();
    private static final String DEFAULT_MANUFACTURER = "Sony";
    private static final String DEFAULT_MODEL = "Bravia";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(2500);
    private static final int DEFAULT_QUANTITY = 5;

    private Electronics createElectronics(UUID id, String manufacturer, String model, BigDecimal price, int quantity) {
        return new Electronics(id, manufacturer, model, price, quantity);
    }

    private Electronics createDefaultElectronics() {
        return createElectronics(DEFAULT_ID, DEFAULT_MANUFACTURER, DEFAULT_MODEL, DEFAULT_PRICE, DEFAULT_QUANTITY);
    }

    private Electronics createWithId(UUID id) {
        return createElectronics(id, DEFAULT_MANUFACTURER, DEFAULT_MODEL, DEFAULT_PRICE, DEFAULT_QUANTITY);
    }

    private Electronics createWithManufacturer(String manufacturer) {
        return createElectronics(DEFAULT_ID, manufacturer, DEFAULT_MODEL, DEFAULT_PRICE, DEFAULT_QUANTITY);
    }

    private Electronics createWithModel(String model) {
        return createElectronics(DEFAULT_ID, DEFAULT_MANUFACTURER, model, DEFAULT_PRICE, DEFAULT_QUANTITY);
    }

    private Electronics createWithPrice(BigDecimal price) {
        return createElectronics(DEFAULT_ID, DEFAULT_MANUFACTURER, DEFAULT_MODEL, price, DEFAULT_QUANTITY);
    }

    private Electronics createWithQuantity(int quantity) {
        return createElectronics(DEFAULT_ID, DEFAULT_MANUFACTURER, DEFAULT_MODEL, DEFAULT_PRICE, quantity);
    }

    @Test
    void shouldCreateObjectWithCorrectData() {
        // Act
        Electronics electronics = createDefaultElectronics();

        // Assert
        assertEquals(DEFAULT_ID, electronics.getId());
        assertEquals(DEFAULT_MANUFACTURER, electronics.getManufacturer());
        assertEquals(DEFAULT_MODEL, electronics.getModel());
        assertEquals("Sony Bravia", electronics.getName());
        assertEquals(DEFAULT_PRICE, electronics.getPrice());
        assertEquals(DEFAULT_QUANTITY, electronics.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> createWithId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenManufacturerIsNull() {
        assertThatThrownBy(() -> createWithManufacturer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Manufacturer cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenManufacturerIsBlank() {
        assertThatThrownBy(() -> createWithManufacturer("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Manufacturer cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenModelIsNull() {
        assertThatThrownBy(() -> createWithModel(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Model cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenModelIsBlank() {
        assertThatThrownBy(() -> createWithModel("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Model cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThatThrownBy(() -> createWithPrice(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null or below zero");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThatThrownBy(() -> createWithPrice(BigDecimal.valueOf(-100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null or below zero");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatThrownBy(() -> createWithQuantity(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be negative");
    }
}