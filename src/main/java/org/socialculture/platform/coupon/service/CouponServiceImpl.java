package org.socialculture.platform.coupon.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 쿠폰 서비스 구현체
 *
 * @author ycjung
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    private static String MEMBER_EMAIL = "ello@test.com"; // 임시 메일 테스트 -> 토큰 발행되면 수정

    @Override
    public List<CouponResponseDto> getAllCouponsByMemberEmail() {

        return couponRepository.getAllCouponsByMemberEmail(MEMBER_EMAIL)
                .stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }
}
