package com.jrlcst.couponapi.coupon.domain.exception;

public class InvalidCouponDescriptionException extends RuntimeException {
    public InvalidCouponDescriptionException(String message) {
        super(message);
    }
}
