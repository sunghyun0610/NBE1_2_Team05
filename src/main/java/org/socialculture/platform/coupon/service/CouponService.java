package org.socialculture.platform.coupon.service;

import org.socialculture.platform.coupon.dto.response.CouponResponseDto;

import java.util.List;

/**
 * 쿠폰 서비스 인터페이스
 *
 * @author ycjung
 */
public interface CouponService {

    List<CouponResponseDto> getAllCouponsByMemberEmail(String email);

    CouponResponseDto getFirstComeCoupon(String username, Long performanceId);
}
