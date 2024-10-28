package org.socialculture.platform.coupon.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.coupon.repository.CouponRepository;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final MemberRepository memberRepository;

    @Override
    public List<CouponResponseDto> getAllCouponsByMemberEmail(String email) {

        return couponRepository.getAllCouponsByMemberEmail(email)
                .stream()
                .map(CouponResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CouponResponseDto getFirstComeCoupon(String email, Long performanceId) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //이미 해당 공연의 선착순 티켓 발급 받은 경우
        if(couponRepository.getCouponByPerformanceIdAndMemberId(performanceId, member.getMemberId()).isPresent()) {
            throw new GeneralException(ErrorStatus._ALREADY_RECEIVED_FIRST_COME_COUPON);
        }

        CouponEntity coupon = couponRepository.getFirstComeCouponByPerformanceId(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._FIRST_COME_COUPON_NOT_FOUND));

        coupon.setMember(member);
        coupon.setExpireTime(LocalDateTime.now().plusDays(3)); //3일 뒤 만료

        return CouponResponseDto.fromEntity(coupon);
    }
}
