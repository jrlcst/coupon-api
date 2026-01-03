package com.jrlcst.couponapi.coupon.infrastructure.persistence.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.infrastructure.persistence.entity.CouponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CouponPersistenceMapperTest {

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @Test
    @DisplayName("Deve mapear cupom para nova entidade")
    void shouldMapToNewEntity() {
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        var entity = CouponPersistenceMapper.toNewEntity(coupon);

        assertEquals(coupon.getId(), entity.getReferenceId());
        assertEquals(coupon.getCode().value(), entity.getCode());
        assertEquals(coupon.getDescription(), entity.getDescription());
        assertEquals(coupon.getDiscount().value(), entity.getDiscountValue());
        assertEquals(coupon.getExpirationDate(), entity.getExpirationDate());
        assertEquals(coupon.isPublished(), entity.isPublished());
        assertEquals(coupon.isDeleted(), entity.isDeleted());
        assertEquals(coupon.getDeletedAt(), entity.getDeletedAt());
    }

    @Test
    @DisplayName("Deve mesclar cupom em entidade existente")
    void shouldMergeWithExistingEntity() {
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        var entity = new CouponEntity();
        entity.setReferenceId(UUID.randomUUID());

        var merged = CouponPersistenceMapper.merge(entity, coupon);

        assertEquals(entity, merged);
        assertEquals(coupon.getCode().value(), merged.getCode());
        assertEquals(coupon.isPublished(), merged.isPublished());
    }

    @Test
    @DisplayName("Deve mapear entidade para domínio")
    void shouldMapToDomain() {
        var entity = new CouponEntity();
        entity.setReferenceId(UUID.randomUUID());
        entity.setCode("ABC123");
        entity.setDescription("Desc");
        entity.setDiscountValue(BigDecimal.valueOf(1.0));
        entity.setExpirationDate(now.plusDays(1));
        entity.setPublished(true);
        entity.setDeleted(false);

        var domain = CouponPersistenceMapper.toDomain(entity);

        assertEquals(entity.getReferenceId(), domain.getId());
        assertEquals(entity.getCode(), domain.getCode().value());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.getDiscountValue(), domain.getDiscount().value());
        assertEquals(entity.getExpirationDate(), domain.getExpirationDate());
        assertEquals(entity.isPublished(), domain.isPublished());
        assertFalse(domain.isDeleted());
    }
}
