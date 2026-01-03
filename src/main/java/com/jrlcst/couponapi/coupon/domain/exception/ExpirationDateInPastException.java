package com.jrlcst.couponapi.coupon.domain.exception;

public class ExpirationDateInPastException extends RuntimeException {
    public ExpirationDateInPastException() {
        super("The expiration date cannot be in the past");
    }
}
