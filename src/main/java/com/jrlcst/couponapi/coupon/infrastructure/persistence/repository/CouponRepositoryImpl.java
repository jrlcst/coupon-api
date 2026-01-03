package com.jrlcst.couponapi.coupon.infrastructure.persistence.repository;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import com.jrlcst.couponapi.coupon.infrastructure.persistence.entity.CouponEntity;
import com.jrlcst.couponapi.coupon.infrastructure.persistence.mapper.CouponPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final SpringCouponJpaRepository jpaRepository;

    @Override
    public Coupon save(final Coupon coupon) {

        final CouponEntity entity = jpaRepository
                .findByReferenceId(coupon.getId())
                .map(existing -> CouponPersistenceMapper.merge(existing, coupon))
                .orElseGet(() -> CouponPersistenceMapper.toNewEntity(coupon));

        final CouponEntity saved = jpaRepository.save(entity);
        return CouponPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Coupon> findById(final UUID id) {
        return jpaRepository.findByReferenceId(id)
                .map(CouponPersistenceMapper::toDomain);
    }
}
