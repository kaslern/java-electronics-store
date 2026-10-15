package com.example.store.service;

import com.example.store.model.*;
import com.example.store.repo.OrderRepository;
import com.example.store.service.discount.DiscountService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartManager cartManager;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DiscountService discountService;
    @InjectMocks
    private OrderService orderService;

    private Cart cart;
    private Smartphone smartphone1;
    private Smartphone smartphone2;
    private Customer customer;
    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address("ul. Gratisowa 8", "Cebulowo", "21-370");
        customer = new Customer(UUID.randomUUID(), "Janusz", "Nosacz", "kiedysToBylo@prl.ru", address);
        cart = new Cart();
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
        smartphone2 = Smartphone.builder()
                .id(UUID.randomUUID())
                .manufacturer("Samsung")
                .model("Galaxy S24")
                .price(BigDecimal.valueOf(4000))
                .quantity(10)
                .color(Color.BLACK)
                .batteryCapacity(BatteryCapacity.BATTERY_4500MAH)
                .accessories(List.of("Charger", "Case"))
                .build();

        cart.addProduct(smartphone1);
        cart.addProduct(smartphone2);
    }

    @Test
    void shouldCreateOrderWithCorrectData() {
        // Arrange
        BigDecimal expectedPrice = BigDecimal.valueOf(9000);
        when(cartManager.calculateTotalPrice(cart)).thenReturn(expectedPrice);
        when(discountService.applyDiscount(any(), any(), any())).thenReturn(expectedPrice);

        // Act
        Order result = orderService.createOrder(customer, cart);

        //Assert & Verify
        assertEquals(customer, result.getCustomer());
        assertEquals(BigDecimal.valueOf(9000), result.getTotalPrice());
        assertEquals(2, result.getItems().size());
        assertTrue(cart.getProducts().isEmpty());
        verify(orderRepository).save(result);
    }

    @Nested
    class ValidationTests {

        @Test
        void shouldThrowExceptionForNullCustomer() {
            // Act & Assert
            assertThatThrownBy(() -> orderService.createOrder(null, cart))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Customer or cart must not be null");
        }

        @Test
        void shouldThrowExceptionForCartIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> orderService.createOrder(customer, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Customer or cart must not be null");
        }

        @Test
        void shouldThrowExceptionForEmptyCart() {
            // Arrange
            Cart emptyCart = new Cart();

            // Act & Assert
            assertThatThrownBy(() -> orderService.createOrder(customer, emptyCart))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Cart is empty");
        }
    }
}