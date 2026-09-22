package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ElectronicsTest {

    @Test
    void shouldCreateObjectWithCorrectData() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Electronics electronics = new Electronics(id, "Monitor", BigDecimal.valueOf(500), 1);

        // Assert
        assertEquals(id, electronics.getId());
        assertEquals("Monitor", electronics.getName());
        assertEquals(BigDecimal.valueOf(500), electronics.getPrice());
        assertEquals(1, electronics.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        // Arrange
        UUID id = null;

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, "Monitor", BigDecimal.valueOf(500), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, null, BigDecimal.valueOf(500), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, "", BigDecimal.valueOf(500), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, "Monitor", null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null or below zero");
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, "Monitor", BigDecimal.valueOf(-100), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price cannot be null or below zero");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        assertThatThrownBy(() -> new Electronics(id, "Monitor", BigDecimal.valueOf(500), -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity cannot be negative");
    }
}

