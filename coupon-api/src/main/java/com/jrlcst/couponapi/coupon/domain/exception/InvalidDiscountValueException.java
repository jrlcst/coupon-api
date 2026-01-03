package com.jrlcst.couponapi.coupon.domain.exception;

public class InvalidDiscountValueException extends RuntimeException {
    public InvalidDiscountValueException() {
        super("The discount value must be at least 0.5");
    }
}
