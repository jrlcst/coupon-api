package com.jrlcst.couponapi.coupon.domain;

import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponCodeException;
import com.jrlcst.couponapi.coupon.domain.model.CouponCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CouponCodeTest {

    @Test
    @DisplayName("Deve criar um código de cupom válido sanitizado")
    void shouldCreateNewValidSanitizedCouponCode() {
        var code = CouponCode.of("ABC-123");
        assertEquals("ABC123", code.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC12", "ABC1234", "ABC-12", "ABC-1234", "!!!", ""})
    @DisplayName("Deve lançar exceção para código inválido")
    void shouldThrowExceptionForInvalidCode(String rawCode) {
        assertThrows(InvalidCouponCodeException.class, () -> CouponCode.of(rawCode));
    }

    @Test
    @DisplayName("Deve lançar exceção para código nulo")
    void shouldThrowExceptionForNullCode() {
        assertThrows(InvalidCouponCodeException.class, () -> CouponCode.of(null));
    }

    @Test
    @DisplayName("Deve converter para maiúsculo")
    void shouldConvertToUpperCase() {
        var code = CouponCode.of("abc123");
        assertEquals("ABC123", code.value());
    }
}
