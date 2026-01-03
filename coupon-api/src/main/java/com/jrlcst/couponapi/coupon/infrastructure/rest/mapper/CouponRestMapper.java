package com.jrlcst.couponapi.coupon.infrastructure.rest.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.infrastructure.rest.dto.response.CouponResponse;

public final class CouponRestMapper {

    private CouponRestMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscount().value(),
                coupon.getExpirationDate(),
                coupon.isPublished()
        );
    }
}
