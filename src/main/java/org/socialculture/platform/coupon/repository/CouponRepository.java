package org.socialculture.platform.coupon.repository;

import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.coupon.repository.querydsl.CouponRepositoryCustom;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 쿠폰 레파지토리
 * 
 * @author ycjung
 */
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CouponRepositoryCustom {
    List<CouponEntity> findByPerformance_PerformanceId(Long performanceId);
}
