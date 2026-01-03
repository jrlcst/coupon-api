package com.jrlcst.couponapi.coupon.application.find;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCouponByIdUseCase {

    private final CouponRepository repository;
    private final Clock clock;

    public Optional<Coupon> execute(final UUID id) {
        return repository.findById(id).filter(coupon -> coupon.isActiveAt(LocalDateTime.now(clock)));
    }
}
