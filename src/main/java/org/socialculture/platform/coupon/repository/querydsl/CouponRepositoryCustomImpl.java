package org.socialculture.platform.coupon.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.coupon.entity.QCouponEntity;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL 을 사용하기 위한 repo impl
 *
 * @author ycjung
 */
@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CouponEntity> getAllCouponsByMemberEmail(String email, Long performanceId) {
        QCouponEntity couponEntity = QCouponEntity.couponEntity;

        return jpaQueryFactory.selectFrom(couponEntity)
                .where(
                        couponEntity.member.email.eq(email)
                                .and(
                                        couponEntity.performance.performanceId.eq(performanceId)
                                                .or(couponEntity.performance.isNull())
                                )
                )
                .fetch();
    }

    @Override
    public Optional<CouponEntity> getFirstComeCouponByPerformanceId(Long performanceId) {
        QCouponEntity couponEntity = QCouponEntity.couponEntity;

        CouponEntity coupon = jpaQueryFactory.selectFrom(couponEntity)
                .where(couponEntity.performance.performanceId.eq(performanceId)
                        .and(couponEntity.member.isNull())) // member_id가 null
                .orderBy(couponEntity.couponId.asc())  // coupon_id로 오름차순 정렬
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)  // 비관적 Lock 적용
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(coupon);
    }

    @Override
    public Optional<CouponEntity> getCouponByPerformanceIdAndMemberId(Long performanceId, Long memberId) {
        QCouponEntity couponEntity = QCouponEntity.couponEntity;

        CouponEntity coupon = jpaQueryFactory.selectFrom(couponEntity)
                .where(couponEntity.performance.performanceId.eq(performanceId)
                        .and(couponEntity.member.memberId.eq(memberId))) // member_id가 지정된 값과 일치
                .fetchOne();

        return Optional.ofNullable(coupon);
    }
}
