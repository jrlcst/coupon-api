package com.jrlcst.couponapi.coupon.application.create;

import com.jrlcst.couponapi.coupon.domain.exception.ExpirationDateInPastException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponCodeException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponDescriptionException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseTest {

    @Mock
    private CouponRepository repository;

    @Mock
    private Clock clock;

    @InjectMocks
    private CreateCouponUseCase useCase;

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @BeforeEach
    void setup() {
        lenient().when(clock.instant()).thenReturn(now.atZone(ZoneId.systemDefault()).toInstant());
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Test
    @DisplayName("Deve criar um cupom com sucesso via use case")
    void shouldCreateCouponWithSuccess() {
        var command = new CreateCouponCommand(
                "ABC-123",
                "Description",
                BigDecimal.valueOf(1.0),
                now.plusDays(10),
                true
        );

        when(repository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = useCase.execute(command);

        assertNotNull(result);
        assertEquals("ABC123", result.getCode().value());
        assertEquals("Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(1.0), result.getDiscount().value());
        assertTrue(result.isPublished());
        verify(repository, times(1)).save(any(Coupon.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com código inválido")
    void shouldThrowExceptionWhenCodeIsInvalid() {
        var command = new CreateCouponCommand(
                "INVALID",
                "Description",
                BigDecimal.valueOf(1.0),
                now.plusDays(10),
                true
        );

        assertThrows(InvalidCouponCodeException.class, () -> useCase.execute(command));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com data de expiração no passado")
    void shouldThrowExceptionWhenExpirationDateIsInPast() {
        var command = new CreateCouponCommand(
                "ABC-123",
                "Description",
                BigDecimal.valueOf(1.0),
                now.minusDays(1),
                true
        );

        assertThrows(ExpirationDateInPastException.class, () -> useCase.execute(command));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com desconto insuficiente")
    void shouldThrowExceptionWhenDiscountIsTooLow() {
        var command = new CreateCouponCommand(
                "ABC-123",
                "Description",
                BigDecimal.valueOf(0.1),
                now.plusDays(10),
                true
        );

        assertThrows(InvalidDiscountValueException.class, () -> useCase.execute(command));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar cupom com descrição vazia")
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        var command = new CreateCouponCommand(
                "ABC-123",
                "",
                BigDecimal.valueOf(1.0),
                now.plusDays(10),
                true
        );

        assertThrows(InvalidCouponDescriptionException.class, () -> useCase.execute(command));
        verify(repository, never()).save(any());
    }
}
