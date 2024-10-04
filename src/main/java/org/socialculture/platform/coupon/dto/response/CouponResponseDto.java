package org.socialculture.platform.coupon.dto.response;

import org.socialculture.platform.coupon.entity.CouponEntity;

import java.time.LocalDateTime;

/**
 * Coupon 에 대한 Response 정보 매핑
 * 
 * @author ycjung
 */
public record CouponResponseDto(
        Long couponId,
        String name,
        int percent,
        boolean isUsed,
        LocalDateTime expireTime,
        LocalDateTime createdAt
) {
    // 정적 팩토리 메서드 of
    public static CouponResponseDto of(Long couponId, String name, int percent, boolean isUsed,
                                       LocalDateTime expireTime, LocalDateTime createdAt) {
        return new CouponResponseDto(
                couponId,
                name,
                percent,
                isUsed,
                expireTime,
                createdAt
        );
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
