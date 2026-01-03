package com.jrlcst.couponapi.coupon.infrastructure.rest.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.infrastructure.rest.dto.response.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CouponRestMapper {

    private final Clock clock;

    public CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscount().value(),
                coupon.getExpirationDate(),
                coupon.statusAt(LocalDateTime.now(clock)),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }
}
