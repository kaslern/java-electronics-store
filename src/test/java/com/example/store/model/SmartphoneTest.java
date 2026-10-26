package com.example.store.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartphoneTest {

    private static final UUID DEFAULT_ID = UUID.randomUUID();
    private static final String DEFAULT_MANUFACTURER = "Samsung";
    private static final String DEFAULT_MODEL = "Galaxy S23";
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(3000);
    private static final int DEFAULT_QUANTITY = 1;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final BatteryCapacity DEFAULT_BATTERY = BatteryCapacity.BATTERY_4500MAH;
    private static final List<String> DEFAULT_ACCESSORIES = List.of("Charger");

    // Jedyne miejsce wywołania konstruktora Smartphone w całej klasie testowej:
    private Smartphone createSmartphone(Color color, BatteryCapacity batteryCapacity, List<String> accessories) {
        return new Smartphone(
                DEFAULT_ID,
                DEFAULT_MANUFACTURER,
                DEFAULT_MODEL,
                DEFAULT_PRICE,
                DEFAULT_QUANTITY,
                color,
                batteryCapacity,
                accessories
        );
    }

    private Smartphone createDefaultSmartphone() {
        return createSmartphone(DEFAULT_COLOR, DEFAULT_BATTERY, DEFAULT_ACCESSORIES);
    }

    @Test
    void shouldCreateObjectWithCorrectData() {
        Smartphone smartphone = createDefaultSmartphone();

        assertEquals(DEFAULT_ID, smartphone.getId());
        assertEquals(DEFAULT_MANUFACTURER, smartphone.getManufacturer());
        assertEquals(DEFAULT_MODEL, smartphone.getModel());
        assertEquals("Samsung Galaxy S23", smartphone.getName());
        assertEquals(DEFAULT_PRICE, smartphone.getPrice());
        assertEquals(DEFAULT_QUANTITY, smartphone.getQuantity());
        assertEquals(DEFAULT_COLOR, smartphone.getColor());
        assertEquals(DEFAULT_BATTERY, smartphone.getBatteryCapacity());
        assertEquals(DEFAULT_ACCESSORIES, smartphone.getAccessories());
    }

    @Test
    void shouldThrowExceptionWhenColorIsNull() {
        assertThatThrownBy(() -> createSmartphone(null, DEFAULT_BATTERY, DEFAULT_ACCESSORIES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Color cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenBatteryCapacityIsNull() {
        assertThatThrownBy(() -> createSmartphone(DEFAULT_COLOR, null, DEFAULT_ACCESSORIES))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Battery capacity cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenAccessoriesIsNull() {
        assertThatThrownBy(() -> createSmartphone(DEFAULT_COLOR, DEFAULT_BATTERY, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Accessories cannot be null");
    }
}