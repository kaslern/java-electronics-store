package com.example.store.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;

public class DiscountService {

    private static final BigDecimal FRIDAY_DISCOUNT_PERCENT = new BigDecimal("0.10");
    private final Map<String, BigDecimal> promoCodes = Map.of(
            "SUMMER10", new BigDecimal("0.10"),
            "VIP20", new BigDecimal("0.20")
    );

    public BigDecimal applyDiscount(BigDecimal basePrice, LocalDateTime orderDate, String promoCode) {
        BigDecimal fridayRate = (orderDate.getDayOfWeek() == DayOfWeek.FRIDAY)
                ? FRIDAY_DISCOUNT_PERCENT
                : BigDecimal.ZERO;

        BigDecimal promoRate = BigDecimal.ZERO;

        if (promoCode != null && !promoCode.isBlank()) {
            promoRate = promoCodes.getOrDefault(promoCode, BigDecimal.ZERO);
        }

        BigDecimal effectiveRate = fridayRate.max(promoRate);

        return basePrice.subtract(basePrice.multiply(effectiveRate));
    }

    public boolean isValidPromoCode(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }
        return promoCodes.containsKey(code.trim().toUpperCase());
    }
}
