package org.socialculture.platform.coupon.repository;

import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.coupon.repository.querydsl.CouponRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 쿠폰 레파지토리
 * 
 * @author ycjung
 */
public interface CouponRepository extends JpaRepository<CouponEntity, Long>, CouponRepositoryCustom {
}
