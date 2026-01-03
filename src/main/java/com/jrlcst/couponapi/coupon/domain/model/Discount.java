package com.jrlcst.couponapi.coupon.domain.model;

import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;

import java.math.BigDecimal;

public record Discount(
        BigDecimal value
) {
    private static final BigDecimal MIN_DISCOUNT_VALUE = new BigDecimal("0.5");

    public static Discount of(final BigDecimal value) {

        if (value == null || value.compareTo(MIN_DISCOUNT_VALUE) < 0) {
            throw new InvalidDiscountValueException();
        }

        return new Discount(value);
    }
}
