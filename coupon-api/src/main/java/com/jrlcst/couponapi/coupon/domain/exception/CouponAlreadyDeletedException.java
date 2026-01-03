package com.jrlcst.couponapi.coupon.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException() {
        super("Coupon already deleted");
    }
}
