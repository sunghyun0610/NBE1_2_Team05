package org.socialculture.platform.performance.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PerformanceWithCategory(
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
}
