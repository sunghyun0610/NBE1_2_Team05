package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.CategoryDTO;
import org.socialculture.platform.performance.entity.CategoryEntity;
import org.socialculture.platform.performance.entity.PerformanceEntity;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PerformanceListResponse(
        String memberName,
        Long performanceId,
        String title,
        LocalDateTime dateStartTime,
        LocalDateTime dateEndTime,
        String address,
        String imageUrl,
        int price,
        String status,
        List<String> category
) {



    public static PerformanceListResponse of(PerformanceEntity performanceEntity) {
        return PerformanceListResponse.builder()
                .memberName(performanceEntity.getMember().getName())
                .performanceId(performanceEntity.getPerformanceId())
                .title(performanceEntity.getTitle())
                .dateStartTime(performanceEntity.getDateStartTime())
                .dateEndTime(performanceEntity.getDateEndTime())
                .address(performanceEntity.getAddress())
                .imageUrl(performanceEntity.getImageUrl())
                .price(performanceEntity.getPrice())
                .status(performanceEntity.getPerformanceStatus().getStatus())
                .category(performanceEntity.getPerformanceCategoryList().stream()
                        .map(e -> e.getCategory().getNameKr()).toList())
                .build();
    }
}
