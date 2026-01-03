package com.jrlcst.couponapi.coupon.infrastructure.persistence.repository;

import com.jrlcst.couponapi.coupon.infrastructure.persistence.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringCouponJpaRepository extends JpaRepository<CouponEntity, UUID> {
    Optional<CouponEntity> findByReferenceId(UUID referenceId);
}
