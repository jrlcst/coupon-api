package com.jrlcst.couponapi.coupon.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(

        @Schema(example = "ABC-123")
        @NotBlank
        String code,

        @Schema(example = "Iure saepe amet. Excepturi saepe inventore nam doloremque voluptatem a.")
        @NotBlank String description, //limitar alto

        @Schema(example = "0.8")
        @NotNull
        @PositiveOrZero
        @DecimalMin(value = "0.5", inclusive = true, message = "The discount value must be at least 0.5")
        BigDecimal discountValue,

        @Schema(example = "2026-11-04T17:14:45.180Z")
        @NotNull LocalDateTime expirationDate,

        @Schema(example = "false")
        boolean published
) {}
