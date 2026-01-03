package com.jrlcst.couponapi.coupon.infrastructure.persistence.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.model.CouponCode;
import com.jrlcst.couponapi.coupon.domain.model.Discount;
import com.jrlcst.couponapi.coupon.infrastructure.persistence.entity.CouponEntity;

public final class CouponPersistenceMapper {

    private CouponPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CouponEntity toNewEntity(final Coupon coupon) {
        final CouponEntity entity = new CouponEntity();
        entity.setReferenceId(coupon.getId());
        applyDomain(entity, coupon);
        return entity;
    }

    public static CouponEntity merge(final CouponEntity entity, final Coupon coupon) {
        applyDomain(entity, coupon);
        return entity;
    }

    private static void applyDomain(final CouponEntity entity, final Coupon coupon) {
        entity.setCode(coupon.getCode().value());
        entity.setDescription(coupon.getDescription());
        entity.setDiscountValue(coupon.getDiscount().value());
        entity.setExpirationDate(coupon.getExpirationDate());
        entity.setPublished(coupon.isPublished());
        entity.setDeleted(coupon.isDeleted());
        entity.setDeletedAt(coupon.getDeletedAt());
    }

    public static Coupon toDomain(final CouponEntity entity) {
        return Coupon.reconstitute(
                entity.getReferenceId(),
                CouponCode.of(entity.getCode()),
                entity.getDescription(),
                Discount.of(entity.getDiscountValue()),
                entity.getExpirationDate(),
                entity.isPublished(),
                entity.isDeleted(),
                entity.getDeletedAt()
        );
    }
}
