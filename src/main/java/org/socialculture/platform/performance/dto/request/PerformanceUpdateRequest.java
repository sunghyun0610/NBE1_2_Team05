package org.socialculture.platform.performance.dto.request;

import lombok.Builder;
import org.socialculture.platform.performance.entity.PerformanceEntity;

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
        Integer maxAudience
) {
    public static PerformanceEntity toEntity(PerformanceUpdateRequest performanceUpdateRequest) {
        return PerformanceEntity.builder()
                .title(performanceUpdateRequest.title())
                .dateStartTime(performanceUpdateRequest.dateStartTime())
                .dateEndTime(performanceUpdateRequest.dateEndTime())
                .address(performanceUpdateRequest.address())
                .imageUrl(performanceUpdateRequest.imageUrl())
                .price(performanceUpdateRequest.price())
                .description(performanceUpdateRequest.description())
                .maxAudience(performanceUpdateRequest.maxAudience())
                .build();

    }
}
