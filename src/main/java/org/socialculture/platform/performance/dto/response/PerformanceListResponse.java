package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.CategoryDto;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;

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
        List<CategoryDto> categories
) {

    public static PerformanceListResponse from(PerformanceWithCategory performanceWithCategory) {
        List<CategoryDto> categoryDtos = performanceWithCategory.getCategories().stream()
                .map(CategoryDto::toDto).toList();

        return PerformanceListResponse.builder()
                .memberName(performanceWithCategory.getMemberName())
                .performanceId(performanceWithCategory.getPerformanceId())
                .title(performanceWithCategory.getTitle())
                .dateStartTime(performanceWithCategory.getDateStartTime())
                .dateEndTime(performanceWithCategory.getDateEndTime())
                .address(performanceWithCategory.getAddress())
                .imageUrl(performanceWithCategory.getImageUrl())
                .price(performanceWithCategory.getPrice())
                .status(String.valueOf(performanceWithCategory.getStatus()))
                .categories(categoryDtos)
                .build();
    }
}
