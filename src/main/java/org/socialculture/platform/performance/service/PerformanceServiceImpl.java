package org.socialculture.platform.performance.service;

import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public List<PerformanceListResponse> getPerformanceList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<PerformanceWithCategory> performanceList = performanceRepository.getPerformanceWithCategoryList(pageRequest);

        return performanceList.stream()
                .map(PerformanceListResponse::from)
                .toList();
    }
}
