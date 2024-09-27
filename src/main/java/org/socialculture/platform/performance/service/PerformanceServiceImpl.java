package org.socialculture.platform.performance.service;

import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public List<PerformanceListResponse> getPerformanceList() {
        List<PerformanceEntity> performanceList = performanceRepository.findAll();

        return performanceList.stream()
                .map(PerformanceListResponse::of)
                .toList();
    }
}
