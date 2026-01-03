package com.jrlcst.couponapi.coupon.infrastructure.persistence.repository;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.infrastructure.persistence.entity.CouponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryImplTest {

    @Mock
    private SpringCouponJpaRepository jpaRepository;

    @InjectMocks
    private CouponRepositoryImpl repository;

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @Test
    @DisplayName("Deve salvar novo cupom")
    void shouldSaveNewCoupon() {
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        
        when(jpaRepository.findByReferenceId(coupon.getId())).thenReturn(Optional.empty());
        when(jpaRepository.save(any(CouponEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = repository.save(coupon);

        assertNotNull(result);
        assertEquals(coupon.getId(), result.getId());
        verify(jpaRepository, times(1)).save(any(CouponEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar cupom existente")
    void shouldUpdateExistingCoupon() {
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        var existingEntity = new CouponEntity();
        existingEntity.setReferenceId(coupon.getId());

        when(jpaRepository.findByReferenceId(coupon.getId())).thenReturn(Optional.of(existingEntity));
        when(jpaRepository.save(any(CouponEntity.class))).thenReturn(existingEntity);

        var result = repository.save(coupon);

        assertNotNull(result);
        verify(jpaRepository, times(1)).save(existingEntity);
    }

    @Test
    @DisplayName("Deve encontrar cupom por ID")
    void shouldFindById() {
        var id = UUID.randomUUID();
        var entity = new CouponEntity();
        entity.setReferenceId(id);
        entity.setCode("ABC123");
        entity.setDiscountValue(BigDecimal.valueOf(1.0));
        entity.setExpirationDate(now.plusDays(1));
        entity.setDescription("Desc");

        when(jpaRepository.findByReferenceId(id)).thenReturn(Optional.of(entity));

        var result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }
}
