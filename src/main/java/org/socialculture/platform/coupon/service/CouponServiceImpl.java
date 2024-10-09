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

    @Override
    public List<CouponResponseDto> getAllCouponsByMemberEmail(String email) {

        return couponRepository.getAllCouponsByMemberEmail(email)
                .stream()
                .map(CouponResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
