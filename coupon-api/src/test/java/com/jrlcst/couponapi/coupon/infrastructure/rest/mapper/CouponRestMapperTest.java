package com.jrlcst.couponapi.coupon.infrastructure.rest.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponRestMapperTest {

    @Test
    @DisplayName("Deve mapear domínio para resposta REST")
    void shouldMapToResponse() {
        var now = LocalDateTime.now();
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        
        var response = CouponRestMapper.toResponse(coupon);

        assertEquals(coupon.getId(), response.id());
        assertEquals(coupon.getCode().value(), response.code());
        assertEquals(coupon.getDescription(), response.description());
        assertEquals(coupon.getDiscount().value(), response.discountValue());
        assertEquals(coupon.getExpirationDate(), response.expirationDate());
        assertEquals(coupon.isPublished(), response.published());
    }
}
