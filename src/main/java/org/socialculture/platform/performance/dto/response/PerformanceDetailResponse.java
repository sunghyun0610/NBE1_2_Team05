package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.performance.dto.CategoryDto;
import org.socialculture.platform.performance.dto.domain.PerformanceDetail;
import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PerformanceDetailResponse(
    String memberName,
    Long performanceId,
    String title,
    LocalDateTime dateStartTime,
    LocalDateTime dateEndTime,
    String description,
    int maxAudience,
    String address,
    String location,
    String imageUrl,
    int price,
    int remainingTickets,
    LocalDateTime startDate,
    PerformanceStatus status,
    boolean updatable,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CategoryDto>categories,
    List<CouponResponseDto> firstComeCoupons
) {
    public static PerformanceDetailResponse from(boolean updatable, PerformanceDetail performanceDetail) {
        List<CategoryDto> categoryDtos = performanceDetail.getCategories().stream()
                .map(CategoryDto::toDto).toList();

        return PerformanceDetailResponse.builder()
                .memberName(performanceDetail.getMemberName())
                .performanceId(performanceDetail.getPerformanceId())
                .title(performanceDetail.getTitle())
                .dateStartTime(performanceDetail.getDateStartTime())
                .dateEndTime(performanceDetail.getDateEndTime())
                .description(performanceDetail.getDescription())
                .maxAudience(performanceDetail.getMaxAudience())
                .address(performanceDetail.getAddress())
                .location(performanceDetail.getLocation())
                .imageUrl(performanceDetail.getImageUrl())
                .price(performanceDetail.getPrice())
                .remainingTickets(performanceDetail.getRemainingTickets())
                .startDate(performanceDetail.getStartDate())
                .status(performanceDetail.getStatus())
                .updatable(updatable)
                .createdAt(performanceDetail.getCreatedAt())
                .updatedAt(performanceDetail.getUpdatedAt())
                .categories(categoryDtos)
                .firstComeCoupons(performanceDetail.getFirstComeCoupons())
                .build();
    }
}
