package com.jrlcst.couponapi.coupon.infrastructure.rest.dto.response;

import com.jrlcst.couponapi.coupon.domain.enumeration.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {}
