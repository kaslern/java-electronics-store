package com.example.store.service;

import com.example.store.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartManagerTest {

    @Mock
    private ProductManager productManager;

    @InjectMocks
    private CartManager cartManager;

    private Cart cart;
    private Smartphone smartphone1;
    private Smartphone smartphone2;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        UUID id2 = UUID.randomUUID();
        List<String> accessories = List.of("Charger");

        smartphone1 = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Apple")
                .model("iPhone 15")
                .price(BigDecimal.valueOf(5000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();
        smartphone2 = Smartphone.builder()
                .id(id2)
                .manufacturer("Samsung")
                .model("Galaxy S24")
                .price(BigDecimal.valueOf(4000))
                .quantity(1)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(accessories)
                .build();
    }

    @Test
    void shouldAddProductToCart() {
        // Arrange
        when(productManager.findById(smartphone1.getId())).thenReturn(smartphone1);

        // Act
        cartManager.addProductToCart(smartphone1.getId(), cart);

        // Assert
        assertEquals(1, cart.getProducts().size());
        assertEquals(smartphone1, cart.getProducts().get(0));
    }

    @Test
    void shouldRemoveProductFromCart() {
        // Arrange
        cart.addProduct(smartphone1);

        // Act
        cartManager.removeProductFromCart(smartphone1.getId(), cart);

        // Assert
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void shouldReturnAllProductsFromCart() {
        // Arrange
        cart.addProduct(smartphone1);
        cart.addProduct(smartphone2);

        // Act
        List<Product> result = cartManager.getAllProductsFromCart(cart);

        // Assert
        assertEquals(2, result.size());
        assertEquals(List.of(smartphone1, smartphone2), result);
    }

    @Test
    void shouldCalculateTotalPriceForProductsInCart() {
        // Arrange
        cart.addProduct(smartphone1);
        cart.addProduct(smartphone2);

        // Act
        BigDecimal totalPrice = cartManager.calculateTotalPrice(cart);

        // Assert
        assertEquals(BigDecimal.valueOf(9000), totalPrice);
    }

    @Test
    void shouldReturnZeroTotalPriceForEmptyCart() {
        // Act
        BigDecimal totalPrice = cartManager.calculateTotalPrice(cart);

        // Assert
        assertEquals(BigDecimal.ZERO, totalPrice);
    }

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowExceptionForAddProductToCartWhenCartIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.addProductToCart(smartphone1.getId(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart and product ID must not be null");
        }

        @Test
        void shouldThrowExceptionForAddProductToCartWhenProductIdIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.addProductToCart(null, cart))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart and product ID must not be null");
        }

        @Test
        void shouldThrowExceptionForRemoveProductFromCartWhenCartIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.removeProductFromCart(smartphone1.getId(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart and product ID must not be null");
        }

        @Test
        void shouldThrowExceptionForRemoveProductFromCartWhenProductIdIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.removeProductFromCart(null, cart))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart and product ID must not be null");
        }

        @Test
        void shouldThrowExceptionForRemoveProductFromCartWhenProductNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.removeProductFromCart(smartphone1.getId(), cart))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Product not found in cart");
        }

        @Test
        void shouldThrowExceptionForGetAllProductsFromCartWhenCartIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.getAllProductsFromCart(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart must not be null");
        }

        @Test
        void shouldThrowExceptionForCalculateTotalPriceWhenCartIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> cartManager.calculateTotalPrice(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cart must not be null");
        }
    }
}