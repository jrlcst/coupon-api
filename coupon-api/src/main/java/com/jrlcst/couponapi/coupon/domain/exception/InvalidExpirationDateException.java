package com.jrlcst.couponapi.coupon.domain.exception;

public class InvalidExpirationDateException extends RuntimeException {
    public InvalidExpirationDateException(String message) {
        super(message);
    }
}
