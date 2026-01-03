package com.jrlcst.couponapi.coupon.infrastructure.rest.controller;

import com.jrlcst.couponapi.coupon.application.create.CreateCouponCommand;
import com.jrlcst.couponapi.coupon.application.create.CreateCouponUseCase;
import com.jrlcst.couponapi.coupon.application.delete.DeleteCouponUseCase;
import com.jrlcst.couponapi.coupon.application.find.GetCouponByIdUseCase;
import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.infrastructure.rest.dto.request.CreateCouponRequest;
import com.jrlcst.couponapi.coupon.infrastructure.rest.dto.response.CouponResponse;
import com.jrlcst.couponapi.coupon.infrastructure.rest.mapper.CouponRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/coupon")
@Tag(name = "Coupon V1 Controller", description = "Endpoints for coupon management (V1)")
public class CouponV1Controller {

    private final CreateCouponUseCase createUseCase;
    private final GetCouponByIdUseCase getUseCase;
    private final DeleteCouponUseCase deleteUseCase;

    private final CouponRestMapper couponRestMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new coupon")
    public ResponseEntity<CouponResponse> create(
            @RequestBody @Valid CreateCouponRequest request
    ) {
        final var command = new CreateCouponCommand(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        final Coupon coupon = createUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(couponRestMapper.toResponse(coupon));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a coupon by ID")
    public ResponseEntity<CouponResponse> getById(@PathVariable UUID id) {
        return getUseCase.execute(id)
                .map(couponRestMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a coupon")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
