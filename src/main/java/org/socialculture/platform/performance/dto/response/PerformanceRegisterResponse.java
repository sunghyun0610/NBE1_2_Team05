package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.CategoryDto;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PerformanceRegisterResponse(
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
        List<CategoryDto> categories
) {
    public static PerformanceRegisterResponse of(PerformanceEntity performanceEntity, List<CategoryDto> categories) {
        return PerformanceRegisterResponse.builder()
                .memberName(performanceEntity.getMember().getName())
                .performanceId(performanceEntity.getPerformanceId())
                .title(performanceEntity.getTitle())
                .dateStartTime(performanceEntity.getDateStartTime())
                .dateEndTime(performanceEntity.getDateEndTime())
                .description(performanceEntity.getDescription())
                .maxAudience(performanceEntity.getMaxAudience())
                .address(performanceEntity.getAddress())
                .imageUrl(performanceEntity.getImageUrl())
                .price(performanceEntity.getPrice())
                .remainingTickets(performanceEntity.getRemainingTickets())
                .startDate(performanceEntity.getStartDate())
                .status(performanceEntity.getPerformanceStatus())
                .createdAt(performanceEntity.getCreatedAt())
                .updatedAt(performanceEntity.getUpdatedAt())
                .categories(categories)
                .build();
    }
}
