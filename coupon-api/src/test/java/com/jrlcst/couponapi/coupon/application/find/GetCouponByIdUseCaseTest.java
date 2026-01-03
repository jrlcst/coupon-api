package com.jrlcst.couponapi.coupon.application.find;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
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
class GetCouponByIdUseCaseTest {

    @Mock
    private CouponRepository repository;

    @Mock
    private Clock clock;

    @InjectMocks
    private GetCouponByIdUseCase useCase;

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @BeforeEach
    void setup() {
        lenient().when(clock.instant()).thenReturn(now.atZone(ZoneId.systemDefault()).toInstant());
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    @DisplayName("Deve encontrar cupom por ID quando ativo")
    void shouldFindCouponByIdWhenActive() {
        var id = UUID.randomUUID();
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        
        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        var result = useCase.execute(id);

        assertTrue(result.isPresent());
        assertEquals(coupon, result.get());
    }

    @Test
    @DisplayName("Deve retornar vazio quando cupom está expirado")
    void shouldReturnEmptyWhenCouponIsExpired() {
        var id = UUID.randomUUID();
        // Criar um cupom que expira AGORA (ou um pouco antes se usarmos o clock)
        // reconstitute permite criar um cupom com data passada sem disparar a exception do createNew
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        
        // Simular que o tempo passou
        var futureNow = now.plusDays(2);
        when(clock.instant()).thenReturn(futureNow.atZone(ZoneId.systemDefault()).toInstant());
        
        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        var result = useCase.execute(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar vazio quando cupom não existe")
    void shouldReturnEmptyWhenCouponDoesNotExist() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = useCase.execute(id);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar vazio quando cupom está deletado")
    void shouldReturnEmptyWhenCouponIsDeleted() {
        var id = UUID.randomUUID();
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, now);
        coupon.delete(now.minusHours(1));

        when(repository.findById(id)).thenReturn(Optional.of(coupon));

        var result = useCase.execute(id);

        assertTrue(result.isEmpty());
    }
}
