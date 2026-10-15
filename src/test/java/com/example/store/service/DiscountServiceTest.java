package com.example.store.service;

import com.example.store.service.discount.DiscountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @Test
    void shouldApplyDiscountInFriday() {
        //Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 9, 12, 0);

        //Act
        BigDecimal priceAfterDiscount = discountService.applyDiscount(price, orderDate, "");

        //Assert
        assertEquals(new BigDecimal("90.00"), priceAfterDiscount);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VIP20", "vip20", " VIP20 ", "Vip20"})
    void shouldApplyDiscountForPromoCode(String promoCode) {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);

        // Act
        BigDecimal priceAfterDiscount = discountService.applyDiscount(price, orderDate, promoCode);

        // Assert
        assertEquals(new BigDecimal("80.00"), priceAfterDiscount);
    }

    @Test
    void shouldNotApplyDiscountOnRegularDayWithoutPromoCode() {
        // Arrange
        BigDecimal price = new BigDecimal("100.00");
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);

        // Act
        BigDecimal priceAfterDiscount = discountService.applyDiscount(price, orderDate, null);

        // Assert
        assertEquals(new BigDecimal("100.00"), priceAfterDiscount);
    }

    @Test
    void shouldChooseHigherDiscountOnFridayWhenPromoCodeIsHigher() {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 9, 12, 0);

        // Act
        BigDecimal priceAfterDiscount = discountService.applyDiscount(price, orderDate, "VIP20");

        // Assert
        assertEquals(new BigDecimal("80.00"), priceAfterDiscount);
    }

    @Test
    void shouldApplyFridayDiscountWhenInvalidPromoCodeProvided() {
        // Arrange
        BigDecimal price = BigDecimal.valueOf(100);
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 9, 12, 0);

        // Act
        BigDecimal priceAfterDiscount = discountService.applyDiscount(price, orderDate, "INVALID_CODE");

        // Assert
        assertEquals(new BigDecimal("90.00"), priceAfterDiscount);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VIP20", "vip20", " VIP20 ", "SUMMER10"})
    void shouldValidateCorrectPromoCodes(String code) {
        // Act & Assert
        assertTrue(discountService.isValidPromoCode(code));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "123", "   "})
    void shouldReturnFalseForInvalidPromoCodes(String code) {
        // Act & Assert
        assertFalse(discountService.isValidPromoCode(code));
    }

    @Test
    void shouldReturnFalseForNullPromoCode() {
        // Act & Assert
        assertFalse(discountService.isValidPromoCode(null));
    }
}