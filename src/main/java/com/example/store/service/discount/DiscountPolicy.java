package com.example.store.service.discount;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;

public enum DiscountPolicy implements DiscountStrategy {

    NONE("No discount") {
        @Override
        public BigDecimal getDiscountRate(LocalDateTime orderDate, String promoCode) {
            return BigDecimal.ZERO;
        }
    },
    FRIDAY("Friday 10% discount") {
        private static final BigDecimal FRIDAY_DISCOUNT_PERCENT = new BigDecimal("0.10");

        @Override
        public BigDecimal getDiscountRate(LocalDateTime orderDate, String promoCode) {
            return (orderDate.getDayOfWeek() == DayOfWeek.FRIDAY)
                    ? FRIDAY_DISCOUNT_PERCENT
                    : BigDecimal.ZERO;
        }
    },
    PROMO_CODE("Promo code") {
        private final Map<String, BigDecimal> promoCodes = Map.of(
                "SUMMER10", new BigDecimal("0.10"),
                "VIP20", new BigDecimal("0.20")
        );

        @Override
        public BigDecimal getDiscountRate(LocalDateTime orderDate, String promoCode) {
            if (promoCode == null || promoCode.isBlank()) {
                return BigDecimal.ZERO;
            }
            return promoCodes.getOrDefault(promoCode.toUpperCase().trim(), BigDecimal.ZERO);
        }

        @Override
        public boolean isValidPromoCode(String code) {
            if (code == null || code.isBlank()) {
                return false;
            }
            return promoCodes.containsKey(code.trim().toUpperCase());
        }
    };

    public boolean isValidPromoCode(String code) {
        return DiscountPolicy.PROMO_CODE.isValidPromoCode(code);
    }

    private final String description;

    DiscountPolicy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}