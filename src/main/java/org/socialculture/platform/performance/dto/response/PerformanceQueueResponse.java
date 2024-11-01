package org.socialculture.platform.performance.dto.response;

import lombok.Builder;

@Builder
public record PerformanceQueueResponse(
        String userEmail,
        Long rank
) {
    public static PerformanceQueueResponse from(String userEmail, Long rank){
        PerformanceQueueResponse performanceQueueResponse = PerformanceQueueResponse.builder()
                .userEmail(userEmail)
                .rank(rank)
                .build();

        return performanceQueueResponse;
    }
}
