package com.jrlcst.couponapi.coupon.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;
import com.jrlcst.couponapi.coupon.domain.enumeration.CouponStatus;
import com.jrlcst.couponapi.coupon.domain.exception.CouponAlreadyDeletedException;
import com.jrlcst.couponapi.coupon.domain.exception.ExpirationDateInPastException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponDescriptionException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidExpirationDateException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Coupon {

    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final Discount discount;
    private final LocalDateTime expirationDate;
    private final boolean published;
    private final boolean redeemed;

    private boolean deleted;
    private LocalDateTime deletedAt;

    private Coupon(
            UUID id,
            CouponCode code,
            String description,
            Discount discount,
            LocalDateTime expirationDate,
            boolean published,
            boolean deleted,
            boolean redeemed,
            LocalDateTime deletedAt
    ) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (code == null) throw new IllegalArgumentException("Code must not be null");
        if (description == null || description.isBlank()) throw new InvalidCouponDescriptionException("Description must not be blank");
        if (expirationDate == null) throw new InvalidExpirationDateException("Expiration date must not be null");
        if (discount == null) throw new IllegalArgumentException("Discount must not be null");

        this.id = id;
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.expirationDate = expirationDate;
        this.published = published;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
        this.redeemed = redeemed;
    }

    public boolean isActiveAt(final LocalDateTime now) {
        return !deleted && expirationDate.isAfter(now);
    }

    public static Coupon reconstitute(
            UUID id,
            CouponCode code,
            String description,
            Discount discount,
            LocalDateTime expirationDate,
            boolean published,
            boolean deleted,
            boolean redeemed,
            LocalDateTime deletedAt
    ) {
        return new Coupon(id, code, description, discount, expirationDate, published, deleted, redeemed, deletedAt);
    }

    public static Coupon createNew(
            String rawCode,
            String description,
            BigDecimal discountValue,
            LocalDateTime expirationDate,
            boolean published,
            LocalDateTime now
    ) {
        if (expirationDate == null || now == null) throw new InvalidExpirationDateException("Dates must not be null");
        if (expirationDate.isBefore(now)) throw new ExpirationDateInPastException();

        return new Coupon(
                UuidCreator.getTimeOrderedEpoch(),
                CouponCode.of(rawCode),
                description,
                Discount.of(discountValue),
                expirationDate,
                published,
                false,
                false,
                null
        );
    }

    public CouponStatus statusAt(LocalDateTime now) {
        if (deleted) return CouponStatus.DELETED;
        if (expirationDate.isBefore(now)) return CouponStatus.EXPIRED;
        if (redeemed) return CouponStatus.REDEEMED;
        return CouponStatus.ACTIVE;
    }

    public void delete(final LocalDateTime now) {
        if (this.isDeleted()) {
            throw new CouponAlreadyDeletedException();
        }
        this.deleted = true;
        this.deletedAt = now;
    }
}
