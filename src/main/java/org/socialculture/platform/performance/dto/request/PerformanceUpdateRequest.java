package org.socialculture.platform.performance.dto.request;

import lombok.Builder;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;

@Builder
public record PerformanceUpdateRequest(
        String title,
        LocalDateTime dateStartTime,
        LocalDateTime dateEndTime,
        String address,
        String imageUrl,
        Integer price,
        String description,
        int remainTickets,
        Integer maxAudience,
        PerformanceStatus status
) {
    public static PerformanceEntity toEntity(PerformanceUpdateRequest performanceUpdateRequest) {
        return PerformanceEntity.builder()
                .title(performanceUpdateRequest.title())
                .dateStartTime(performanceUpdateRequest.dateStartTime())
                .dateEndTime(performanceUpdateRequest.dateEndTime())
                .address(performanceUpdateRequest.address())
                .imageUrl(performanceUpdateRequest.imageUrl())
                .price(performanceUpdateRequest.price())
                .remainingTickets(performanceUpdateRequest.remainTickets)
                .description(performanceUpdateRequest.description())
                .maxAudience(performanceUpdateRequest.maxAudience())
                .performanceStatus(performanceUpdateRequest.status())
                .build();

    }
}
