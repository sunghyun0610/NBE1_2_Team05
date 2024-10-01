package org.socialculture.platform.performance.service;

import org.socialculture.platform.performance.dto.response.PerformanceDetailResponse;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;

import java.util.List;

public interface PerformanceService {
    List<PerformanceListResponse> getPerformanceList(Integer page, Integer size);

    PerformanceDetailResponse getPerformanceDetail(Long performanceId);
}
