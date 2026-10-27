package com.example.store.service.discount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DiscountStrategy {
    public BigDecimal getDiscountRate(LocalDateTime orderDate, String promoCode);
}