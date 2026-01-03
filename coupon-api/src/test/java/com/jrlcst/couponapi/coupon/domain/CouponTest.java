package com.jrlcst.couponapi.coupon.domain;

import com.jrlcst.couponapi.coupon.domain.exception.CouponAlreadyDeletedException;
import com.jrlcst.couponapi.coupon.domain.exception.ExpirationDateInPastException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponDescriptionException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidExpirationDateException;
import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.model.CouponCode;
import com.jrlcst.couponapi.coupon.domain.model.Discount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private final LocalDateTime now = LocalDateTime.of(2025, 1, 1, 10, 0);

    @Test
    @DisplayName("Deve criar um cupom com sucesso")
    void shouldCreateNewCouponWithSuccess() {
        var expirationDate = now.plusDays(10);
        var coupon = Coupon.createNew(
                "ABC-123",
                "Descrição do cupom",
                BigDecimal.valueOf(10.0),
                expirationDate,
                true,
                now
        );

        assertNotNull(coupon.getId());
        assertEquals("ABC123", coupon.getCode().value());
        assertEquals(BigDecimal.valueOf(10.0), coupon.getDiscount().value());
        assertEquals(expirationDate, coupon.getExpirationDate());
        assertTrue(coupon.isPublished());
        assertFalse(coupon.isDeleted());
    }

    @Test
    @DisplayName("Deve lançar exceção para valor de desconto menor que 0.5")
    void shouldThrowExceptionForLowDiscountValue() {
        assertThrows(InvalidDiscountValueException.class, () ->
                Coupon.createNew(
                        "ABC123",
                        "Desc",
                        BigDecimal.valueOf(0.49),
                        now.plusDays(1),
                        true,
                        now
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção para data de expiração no passado")
    void shouldThrowExceptionForPastExpirationDate() {
        assertThrows(ExpirationDateInPastException.class, () ->
                Coupon.createNew(
                        "ABC123",
                        "Desc",
                        BigDecimal.valueOf(10.0),
                        now.minusSeconds(1),
                        true,
                        now
                )
        );
    }

    @Test
    @DisplayName("Deve realizar soft delete com sucesso")
    void shouldPerformSoftDelete() {
        var coupon = Coupon.createNew(
                "ABC123",
                "Desc",
                BigDecimal.valueOf(10.0),
                now.plusDays(1),
                true,
                now
        );

        coupon.delete(now);

        assertTrue(coupon.isDeleted());
        assertEquals(now, coupon.getDeletedAt());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar cupom já deletado")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        var coupon = Coupon.createNew(
                "ABC123",
                "Desc",
                BigDecimal.valueOf(10.0),
                now.plusDays(1),
                true,
                now
        );

        coupon.delete(now);

        assertThrows(CouponAlreadyDeletedException.class, () -> coupon.delete(now));
    }

    @Test
    @DisplayName("Deve reconstituir um cupom com sucesso")
    void shouldReconstituteCoupon() {
        var id = UUID.randomUUID();
        var code = CouponCode.of("ABC123");
        var discount = Discount.of(BigDecimal.valueOf(1.0));
        var expirationDate = now.plusDays(1);
        var deletedAt = now.minusDays(1);

        var coupon = Coupon.reconstitute(
                id,
                code,
                "Desc",
                discount,
                expirationDate,
                true,
                true,
                deletedAt
        );

        assertEquals(id, coupon.getId());
        assertEquals(code, coupon.getCode());
        assertEquals("Desc", coupon.getDescription());
        assertEquals(discount, coupon.getDiscount());
        assertEquals(expirationDate, coupon.getExpirationDate());
        assertTrue(coupon.isPublished());
        assertTrue(coupon.isDeleted());
        assertEquals(deletedAt, coupon.getDeletedAt());
    }

    @Test
    @DisplayName("Deve validar parâmetros no construtor ao reconstituir")
    void shouldValidateParamsOnReconstitute() {
        var id = UUID.randomUUID();
        var code = CouponCode.of("ABC123");
        var discount = Discount.of(BigDecimal.valueOf(1.0));
        var expirationDate = now.plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> Coupon.reconstitute(null, code, "Desc", discount, expirationDate, true, false, null));
        assertThrows(IllegalArgumentException.class, () -> Coupon.reconstitute(id, null, "Desc", discount, expirationDate, true, false, null));
        assertThrows(InvalidCouponDescriptionException.class, () -> Coupon.reconstitute(id, code, null, discount, expirationDate, true, false, null));
        assertThrows(InvalidCouponDescriptionException.class, () -> Coupon.reconstitute(id, code, " ", discount, expirationDate, true, false, null));
        assertThrows(IllegalArgumentException.class, () -> Coupon.reconstitute(id, code, "Desc", null, expirationDate, true, false, null));
        assertThrows(InvalidExpirationDateException.class, () -> Coupon.reconstitute(id, code, "Desc", discount, null, true, false, null));
    }

    @Test
    @DisplayName("Deve retornar se o cupom está ativo corretamente")
    void shouldReturnIsActiveAtCorrectly() {
        var expirationDate = now.plusDays(1);
        var coupon = Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), expirationDate, true, now);

        assertTrue(coupon.isActiveAt(now));
        assertTrue(coupon.isActiveAt(now.plusHours(23)));
        assertFalse(coupon.isActiveAt(now.plusDays(1).plusSeconds(1)));

        coupon.delete(now);
        assertFalse(coupon.isActiveAt(now));
    }

    @Test
    @DisplayName("Deve validar datas nulas no createNew")
    void shouldValidateNullDatesOnCreateNew() {
        assertThrows(InvalidExpirationDateException.class, () ->
                Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), null, true, now));
        assertThrows(InvalidExpirationDateException.class, () ->
                Coupon.createNew("ABC123", "Desc", BigDecimal.valueOf(1.0), now.plusDays(1), true, null));
    }
}
