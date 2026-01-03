package com.jrlcst.couponapi.coupon.infrastructure.rest.mapper;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class CouponRestMapperTest {

    private CouponRestMapper mapper;
    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @BeforeEach
    void setup() {
        Clock clock = Clock.fixed(now.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        mapper = new CouponRestMapper(clock);
    }

    @Test
    @DisplayName("Deve mapear domínio para resposta REST")
    void shouldMapToResponse() {
        var expirationDate = now.plusDays(1);
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), expirationDate, true, now);
        
        var response = mapper.toResponse(coupon);

        assertEquals(coupon.getId(), response.id());
        assertEquals(coupon.getCode().value(), response.code());
        assertEquals(coupon.getDescription(), response.description());
        assertEquals(coupon.getDiscount().value(), response.discountValue());
        assertEquals(coupon.getExpirationDate(), response.expirationDate());
        assertEquals(coupon.isPublished(), response.published());
        assertFalse(response.redeemed());
        assertEquals("ACTIVE", response.status().name());
    }
}
