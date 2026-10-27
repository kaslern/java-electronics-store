package com.example.store.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class DiscountService {

    public BigDecimal applyDiscount(BigDecimal basePrice, LocalDateTime orderDate, String promoCode) {
        BigDecimal maxRate = BigDecimal.ZERO;
        for (DiscountPolicy discount : DiscountPolicy.values()) {
            BigDecimal rate = discount.getDiscountRate(orderDate, promoCode);
            maxRate = maxRate.max(rate);
        }
        return basePrice.subtract(basePrice.multiply(maxRate)).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isValidPromoCode(String code) {
        return DiscountPolicy.PROMO_CODE.isValidPromoCode(code);
    }
}
