package org.socialculture.platform.performance.dto.response;

import lombok.Builder;
import org.socialculture.platform.performance.dto.domain.CategoryContent;
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
        List<CategoryContent> categories
) {
    public PerformanceRegisterResponse of(PerformanceEntity performanceEntity, List<CategoryContent> categories) {

    }
}
