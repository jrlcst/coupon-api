package com.jrlcst.couponapi.coupon.domain.model;

import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponCodeException;

public record CouponCode(String value) {

    public static final int LENGTH = 6;

    public static CouponCode of(final String rawCode) {

        if (rawCode == null || rawCode.isBlank()) {
            throw new InvalidCouponCodeException();
        }

        final String normalized = normalize(rawCode);

        if (normalized.length() != LENGTH) {
            throw new InvalidCouponCodeException();
        }

        return new CouponCode(normalized);
    }

    private static String normalize(final String raw) {
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toUpperCase(c));
            }
        }

        return sb.toString();
    }
}
