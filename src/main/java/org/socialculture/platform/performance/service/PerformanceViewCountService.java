package org.socialculture.platform.performance.service;

import org.socialculture.platform.performance.dto.response.PerformanceListResponse;

import java.util.List;

public interface PerformanceViewCountService {
    // 공연 조회시 해당 performanceId 값을 List에 추가
    void incrementViewCount(Long performanceId);

    // 최근 조회수중 가장 높은 10개의 공연 조회
    PerformanceListResponse getTopPerformanceIds();
}
