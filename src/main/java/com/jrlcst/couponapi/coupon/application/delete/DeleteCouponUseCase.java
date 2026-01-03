package com.jrlcst.couponapi.coupon.application.delete;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import com.jrlcst.couponapi.shared.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponRepository repository;
    private final Clock clock;

    public void execute(final UUID id) {
        log.info("Deleting coupon with id {}", id);

        final Coupon coupon = repository.findById(id).orElseThrow(() -> new NotFoundException(Coupon.class));

        coupon.delete(LocalDateTime.now(clock));
        repository.save(coupon);
    }
}
