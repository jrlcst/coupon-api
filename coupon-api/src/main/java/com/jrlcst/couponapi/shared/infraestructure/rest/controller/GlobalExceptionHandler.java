package com.jrlcst.couponapi.shared.infraestructure.rest.controller;

import com.jrlcst.couponapi.coupon.domain.exception.CouponAlreadyDeletedException;
import com.jrlcst.couponapi.coupon.domain.exception.ExpirationDateInPastException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponCodeException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidCouponDescriptionException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidDiscountValueException;
import com.jrlcst.couponapi.coupon.domain.exception.InvalidExpirationDateException;
import com.jrlcst.couponapi.shared.application.DefaultResponseFactory;
import com.jrlcst.couponapi.shared.domain.exception.NotFoundException;
import com.jrlcst.couponapi.shared.infraestructure.rest.dto.DefaultResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        if (ex.getBindingResult().getErrorCount() > 1) {
            return ResponseEntity.badRequest().body(
                    DefaultResponseFactory.error("Validation failed")
            );
        }

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        log.error("Validation error: {}", errorMessage);

        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(errorMessage)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleUnexpectedException(Exception ex) {

        log.error("Unexpected error while processing request", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DefaultResponseFactory.error(
                        "An error occurred while processing your request"
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex
    ) {
        log.error("Error reading message: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error("Validation failed")
        );
    }

    @ExceptionHandler(InvalidDiscountValueException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleInvalidDiscountValue(
            InvalidDiscountValueException ex
    ) {
        log.error("O valor de desconto deve ser no mínimo 0.5", ex);
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(ExpirationDateInPastException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleExpirationDateInPast(
            ExpirationDateInPastException ex
    ) {
        log.error("A data de expiração não pode ser no passado", ex);
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidCouponCodeException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleInvalidCouponCode(
            InvalidCouponCodeException ex
    ) {
        log.error("O código do cupom é inválido", ex);
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidCouponDescriptionException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleInvalidCouponDescription(
            InvalidCouponDescriptionException ex
    ) {
        log.error("A descrição do cupom é inválida", ex);
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidExpirationDateException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleInvalidExpirationDate(
            InvalidExpirationDateException ex
    ) {
        log.error("A data de expiração do cupom é inválida", ex);
        return ResponseEntity.badRequest().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleCouponAlreadyDeleted(
            CouponAlreadyDeletedException ex
    ) {
        log.error("O cupom já foi deletado", ex);
        return ResponseEntity.unprocessableContent().body(
                DefaultResponseFactory.error(ex.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultResponseDto<Void>> handleNotFoundException(
            NotFoundException ex
    ) {
        log.error("Recurso não encontrado", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                DefaultResponseFactory.notFound(ex.getMessage())
        );
    }
}
