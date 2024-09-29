package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.CategoryDTO;
import org.socialculture.platform.performance.dto.PerformanceWithCategory;

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
        List<CategoryDTO> category
) {

    public static PerformanceListResponse from(PerformanceWithCategory performanceWithCategory) {
        return PerformanceListResponse.builder()
                .memberName(performanceWithCategory.memberName())
                .performanceId(performanceWithCategory.performanceId())
                .title(performanceWithCategory.title())
                .dateStartTime(performanceWithCategory.dateStartTime())
                .dateEndTime(performanceWithCategory.dateEndTime())
                .address(performanceWithCategory.address())
                .imageUrl(performanceWithCategory.imageUrl())
                .price(performanceWithCategory.price())
                .status(performanceWithCategory.status())
                .category(performanceWithCategory.category())
                .build();
    }
}
