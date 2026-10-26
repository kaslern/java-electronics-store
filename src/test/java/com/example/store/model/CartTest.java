package com.example.store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private Smartphone smartphone;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        smartphone = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("iPhone 18")
                .price(BigDecimal.valueOf(5000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();
    }

    @Test
    void shouldAddProductToCart() {
        // Act
        cart.addProduct(smartphone);

        // Assert
        assertEquals(1, cart.getProducts().size());
        assertEquals(smartphone, cart.getProducts().get(0));
    }

    @Test
    void shouldRemoveExistingProductFromCart() {
        // Arrange
        cart.addProduct(smartphone);

        // Act
        boolean result = cart.removeProduct(smartphone.getId());

        // Assert
        assertTrue(result);
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void shouldReturnFalseWhenRemovingNonExistingProduct() {
        // Act
        boolean result = cart.removeProduct(UUID.randomUUID());

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnUnmodifiableListOfProducts() {
        // Arrange
        cart.addProduct(smartphone);

        // Act & Assert
        assertThatThrownBy(() -> cart.getProducts().add(smartphone))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Nested
    class ValidationTests {

        @Test
        void shouldNotAddNullProduct() {
            // Act
            cart.addProduct(null);

            // Assert
            assertTrue(cart.getProducts().isEmpty());
        }

        @Test
        void shouldReturnFalseForRemoveProductWithNullId() {
            // Act
            boolean result = cart.removeProduct(null);

            // Assert
            assertFalse(result);
        }
    }
}