package com.example.store.service;

import com.example.store.service.discount.DiscountPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DiscountPolicyTest {

    @Test
    void shouldReturnZeroForNonePolicy() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 9, 12, 0);
        BigDecimal rate = DiscountPolicy.NONE.getDiscountRate(orderDate, "VIP20");
        assertEquals(BigDecimal.ZERO, rate);
    }

    @Test
    void shouldReturnTenPercentForFridayPolicyOnFriday() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 9, 12, 0);
        BigDecimal rate = DiscountPolicy.FRIDAY.getDiscountRate(orderDate, null);
        assertEquals(new BigDecimal("0.10"), rate);
    }

    @Test
    void shouldReturnZeroForFridayPolicyOnRegularDay() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);
        BigDecimal rate = DiscountPolicy.FRIDAY.getDiscountRate(orderDate, null);
        assertEquals(BigDecimal.ZERO, rate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VIP20", "vip20", " VIP20 "})
    void shouldReturnDiscountRateForValidPromoCode(String promoCode) {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);
        BigDecimal rate = DiscountPolicy.PROMO_CODE.getDiscountRate(orderDate, promoCode);
        assertEquals(new BigDecimal("0.20"), rate);
    }

    @Test
    void shouldReturnZeroForInvalidPromoCode() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);
        BigDecimal rate = DiscountPolicy.PROMO_CODE.getDiscountRate(orderDate, "INVALID");
        assertEquals(BigDecimal.ZERO, rate);
    }

    @Test
    void shouldReturnZeroForNullPromoCode() {
        LocalDateTime orderDate = LocalDateTime.of(2026, 10, 12, 12, 0);
        BigDecimal rate = DiscountPolicy.PROMO_CODE.getDiscountRate(orderDate, null);
        assertEquals(BigDecimal.ZERO, rate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VIP20", "vip20", " VIP20 ", "SUMMER10"})
    void shouldValidateCorrectPromoCodes(String code) {
        assertTrue(DiscountPolicy.PROMO_CODE.isValidPromoCode(code));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "123", "   "})
    void shouldReturnFalseForInvalidPromoCodes(String code) {
        assertFalse(DiscountPolicy.PROMO_CODE.isValidPromoCode(code));
    }

    @Test
    void shouldReturnFalseForNullPromoCodeValidation() {
        assertFalse(DiscountPolicy.PROMO_CODE.isValidPromoCode(null));
    }

    @Test
    void shouldReturnDescription() {
        assertEquals("Friday 10% discount", DiscountPolicy.FRIDAY.getDescription());
    }
}