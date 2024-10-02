package org.socialculture.platform.performance.dto.response;

public record PerformanceUpdateResponse(Long performanceId) {
    public static PerformanceUpdateResponse from(Long performanceId) {
        return new PerformanceUpdateResponse(performanceId);
    }
}
