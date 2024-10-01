package org.socialculture.platform.coupon.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.entity.CouponEntity;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 repo impl
 *
 * @author ycjung
 */
@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CouponEntity> getAllCouponsByMemberEmail(String email) {
        

        return List.of();
    }
}
