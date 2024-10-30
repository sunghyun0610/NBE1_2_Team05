package org.socialculture.platform.performance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.socialculture.platform.performance.dto.CategoryDto;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceAroundPointResponse {

    private String memberName;
    private Long performanceId;
    private String title;
    private LocalDateTime dateStartTime;
    private LocalDateTime dateEndTime;
    private String address;
    private String imageUrl;
    private int price;
    private String status;
    private int remainingTicket;
    private List<CategoryDto> categories;

    public static PerformanceAroundPointResponse from(PerformanceWithCategory performanceWithCategory) {
        List<CategoryDto> categoryDtos = performanceWithCategory.getCategories().stream()
                .map(CategoryDto::toDto).toList();

        return PerformanceAroundPointResponse.builder()
                .memberName(performanceWithCategory.getMemberName())
                .performanceId(performanceWithCategory.getPerformanceId())
                .title(performanceWithCategory.getTitle())
                .dateStartTime(performanceWithCategory.getDateStartTime())
                .dateEndTime(performanceWithCategory.getDateEndTime())
                .address(performanceWithCategory.getAddress())
                .imageUrl(performanceWithCategory.getImageUrl())
                .price(performanceWithCategory.getPrice())
                .status(String.valueOf(performanceWithCategory.getStatus()))
                .remainingTicket(performanceWithCategory.getRemainingTicket())
                .categories(categoryDtos)
                .build();
    }
}
