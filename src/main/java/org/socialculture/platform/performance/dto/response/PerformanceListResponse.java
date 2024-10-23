package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.socialculture.platform.performance.dto.CategoryDto;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PerformanceListResponse {

    private long totalElements;
    private List<PerformanceList> performanceList;

    @Getter
    @Builder
    public static class PerformanceList {
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

        public static PerformanceList from(PerformanceWithCategory performanceWithCategory) {
            List<CategoryDto> categoryDtos = performanceWithCategory.getCategories().stream()
                    .map(CategoryDto::toDto).toList();

            return PerformanceList.builder()
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

    public static PerformanceListResponse from(long totalElements, List<PerformanceWithCategory> performanceWithCategory) {
        List<PerformanceList> performanceList = performanceWithCategory.stream()
                .map(PerformanceList::from)
                .toList();

        return PerformanceListResponse.builder()
                .totalElements(totalElements)
                .performanceList(performanceList)
                .build();
    }

}
