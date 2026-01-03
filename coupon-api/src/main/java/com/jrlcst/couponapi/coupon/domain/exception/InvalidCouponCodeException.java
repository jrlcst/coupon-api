package com.jrlcst.couponapi.coupon.domain.exception;

public class InvalidCouponCodeException extends RuntimeException {
    public InvalidCouponCodeException() {
        super("The coupon code must contain exactly 6 alphanumeric characters");
    }
}
