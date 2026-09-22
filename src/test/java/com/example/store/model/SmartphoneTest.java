package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartphoneTest {

    @Test
    void shouldCreateObjectWithCorrectData() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<String> accessories = List.of("Charger");

        // Act
        Smartphone smartphone = new Smartphone(id, "Samsung", BigDecimal.valueOf(3000), 1, Color.BLACK,
                BatteryCapacity.BATTERY_4500MAH, accessories);

        // Assert
        assertEquals(id, smartphone.getId());
        assertEquals("Samsung", smartphone.getName());
        assertEquals(BigDecimal.valueOf(3000), smartphone.getPrice());
        assertEquals(1, smartphone.getQuantity());
        assertEquals(Color.BLACK, smartphone.getColor());
        assertEquals(BatteryCapacity.BATTERY_4500MAH, smartphone.getBatteryCapacity());
        assertEquals(accessories, smartphone.getAccessories());
    }

    @Test
    void shouldThrowExceptionWhenColorIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<String> accessories = List.of("Charger");

        // Act & Assert
        assertThatThrownBy(() -> new Smartphone(id, "Samsung", BigDecimal.valueOf(3000), 1, null,
                BatteryCapacity.BATTERY_4500MAH, accessories))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Color cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenBatteryCapacityIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<String> accessories = List.of("Charger");

        // Act & Assert
        assertThatThrownBy(() -> new Smartphone(id, "Samsung", BigDecimal.valueOf(3000), 1, Color.BLACK,
                null, accessories))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Battery capacity cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenAccessoriesIsNull() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<String> accessories = null;

        // Act & Assert
        assertThatThrownBy(() -> new Smartphone(id, "Samsung", BigDecimal.valueOf(3000), 1, Color.BLACK,
                BatteryCapacity.BATTERY_4500MAH, accessories))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Accessories cannot be null");
    }

}