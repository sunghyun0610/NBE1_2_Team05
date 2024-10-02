package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.CategoryContent;
import org.socialculture.platform.performance.dto.PerformanceDetail;
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
    String imageUrl,
    int price,
    int remainingTickets,
    LocalDateTime startDate,
    PerformanceStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CategoryContent>categories
) {
    public static PerformanceDetailResponse from(PerformanceDetail performanceDetail) {
        return PerformanceDetailResponse.builder()
                .memberName(performanceDetail.getMemberName())
                .performanceId(performanceDetail.getPerformanceId())
                .title(performanceDetail.getTitle())
                .dateStartTime(performanceDetail.getDateStartTime())
                .dateEndTime(performanceDetail.getDateEndTime())
                .description(performanceDetail.getDescription())
                .maxAudience(performanceDetail.getMaxAudience())
                .address(performanceDetail.getAddress())
                .imageUrl(performanceDetail.getImageUrl())
                .price(performanceDetail.getPrice())
                .remainingTickets(performanceDetail.getRemainingTickets())
                .startDate(performanceDetail.getStartDate())
                .status(performanceDetail.getStatus())
                .createdAt(performanceDetail.getCreatedAt())
                .updatedAt(performanceDetail.getUpdatedAt())
                .categories(performanceDetail.getCategories())
                .build();
    }
}
