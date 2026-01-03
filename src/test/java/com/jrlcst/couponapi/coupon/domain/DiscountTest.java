package com.jrlcst.couponapi.coupon.domain;

import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;
import com.jrlcst.couponapi.coupon.domain.model.Discount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    @Test
    @DisplayName("Deve criar um desconto válido")
    void shouldCreateValidDiscount() {
        var value = new BigDecimal("0.5");
        var discount = Discount.of(value);
        assertEquals(value, discount.value());
    }

    @Test
    @DisplayName("Deve criar um desconto com valor superior ao mínimo")
    void shouldCreateDiscountAboveMinimum() {
        var value = new BigDecimal("10.0");
        var discount = Discount.of(value);
        assertEquals(value, discount.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.49", "0.0", "-1.0"})
    @DisplayName("Deve lançar exceção para valor de desconto inválido")
    void shouldThrowExceptionForInvalidDiscountValue(String value) {
        var bigDecimalValue = new BigDecimal(value);
        assertThrows(InvalidDiscountValueException.class, () -> Discount.of(bigDecimalValue));
    }

    @Test
    @DisplayName("Deve lançar exceção para valor de desconto nulo")
    void shouldThrowExceptionForNullDiscountValue() {
        assertThrows(InvalidDiscountValueException.class, () -> Discount.of(null));
    }
}
