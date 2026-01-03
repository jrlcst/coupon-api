package com.jrlcst.couponapi.coupon.application.delete;

import com.jrlcst.couponapi.coupon.domain.exception.CouponAlreadyDeletedException;
import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import com.jrlcst.couponapi.shared.domain.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepository repository;

    @Mock
    private Clock clock;

    @InjectMocks
    private DeleteCouponUseCase useCase;

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @BeforeEach
    void setup() {
        lenient().when(clock.instant()).thenReturn(now.atZone(ZoneId.systemDefault()).toInstant());
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    @DisplayName("Deve deletar cupom com sucesso")
    void shouldDeleteCouponWithSuccess() {
        var id = UUID.randomUUID();
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        
        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        useCase.execute(id);

        assertTrue(coupon.isDeleted());
        assertEquals(now, coupon.getDeletedAt());
        verify(repository, times(1)).save(coupon);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando cupom não existe")
    void shouldThrowNotFoundExceptionWhenCouponDoesNotExist() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar CouponAlreadyDeletedException quando cupom já deletado")
    void shouldThrowCouponAlreadyDeletedExceptionWhenCouponIsAlreadyDeleted() {
        var id = UUID.randomUUID();
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        coupon.delete(now.minusHours(1));
        
        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> useCase.execute(id));
        verify(repository, never()).save(any(Coupon.class));
    }
}
