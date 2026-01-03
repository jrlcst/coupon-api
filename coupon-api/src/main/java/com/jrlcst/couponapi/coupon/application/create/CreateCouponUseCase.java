package com.jrlcst.couponapi.coupon.application.create;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCouponUseCase {

    private final CouponRepository repository;
    private final Clock clock;

    public Coupon execute(final CreateCouponCommand command) {
        log.info("Creating coupon with code {} and discount value {} and expiration date {} and published {}",
                command.code(), command.discountValue(), command.expirationDate(), command.published());

        final Coupon coupon = Coupon.createNew(
                command.code(),
                command.description(),
                command.discountValue(),
                command.expirationDate(),
                command.published(),
                LocalDateTime.now(clock)
        );

        return repository.save(coupon);
    }
}
