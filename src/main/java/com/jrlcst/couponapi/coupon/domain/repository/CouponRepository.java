package com.jrlcst.couponapi.coupon.domain.repository;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Coupon save(final Coupon coupon);

    Optional<Coupon> findById(final UUID id);
}
