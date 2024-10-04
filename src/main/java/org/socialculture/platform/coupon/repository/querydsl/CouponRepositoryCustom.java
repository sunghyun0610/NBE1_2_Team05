package org.socialculture.platform.coupon.repository.querydsl;

import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.entity.CouponEntity;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 repo
 *
 * @author ycjung
 */
public interface CouponRepositoryCustom {

    List<CouponEntity> getAllCouponsByMemberEmail(String email);
}
