package org.socialculture.platform.coupon.dto.response;

import lombok.Builder;
import org.socialculture.platform.coupon.entity.CouponEntity;

import java.time.LocalDateTime;

/**
 * Coupon 에 대한 Response 정보 매핑
 * 
 * @author ycjung
 */
@Builder
public record CouponResponseDto(
        Long couponId,
        String name,
        int percent,
        boolean isUsed,
        LocalDateTime expireTime,
        LocalDateTime createdAt
) {
    // 정적 팩토리 메서드 create
    public static CouponResponseDto create(Long couponId, String name, int percent, boolean isUsed,
                                           LocalDateTime expireTime, LocalDateTime createdAt) {
        return CouponResponseDto.builder()
                .couponId(couponId)
                .name(name)
                .percent(percent)
                .isUsed(isUsed)
                .expireTime(expireTime)
                .createdAt(createdAt)
                .build();
    }

    // 엔티티로부터 DTO를 생성하는 메서드 from
    public static CouponResponseDto fromEntity(CouponEntity couponEntity) {
        return new CouponResponseDto(
                couponEntity.getCouponId(),
                couponEntity.getName(),
                couponEntity.getPercent(),
                couponEntity.isUsed(),
                couponEntity.getExpireTime(),
                couponEntity.getCreatedAt()
        );
    }
}
