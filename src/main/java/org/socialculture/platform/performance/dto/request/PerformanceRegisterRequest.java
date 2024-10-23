package org.socialculture.platform.performance.dto.request;

import lombok.Builder;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.entity.PerformanceStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PerformanceRegisterRequest(
        String title,
        LocalDateTime dateStartTime,
        LocalDateTime dateEndTime,
        String address,
        String imageUrl,
        int price,
        String description,
        int maxAudience,
        LocalDateTime startDate,
        List<Long> categories
) {
    public PerformanceEntity toEntity(PerformanceRegisterRequest performanceRegisterRequest) {
        return PerformanceEntity.builder()
                .title(performanceRegisterRequest.title)
                .dateStartTime(performanceRegisterRequest.dateStartTime)
                .dateEndTime(performanceRegisterRequest.dateEndTime)
                .description(performanceRegisterRequest.description)
                .maxAudience(performanceRegisterRequest.maxAudience)
                .address(performanceRegisterRequest.address)
                .imageUrl(performanceRegisterRequest.imageUrl)
                .price(performanceRegisterRequest.price)
                .remainingTickets(performanceRegisterRequest.maxAudience)
                .startDate(performanceRegisterRequest.startDate)
                .performanceStatus(PerformanceStatus.NOT_CONFIRMED)
                .build();
    }
}
