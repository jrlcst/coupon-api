package com.jrlcst.couponapi.shared.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class<?> clazz) {
        super(String.format("%s not found", clazz.getSimpleName()));
    }
}
