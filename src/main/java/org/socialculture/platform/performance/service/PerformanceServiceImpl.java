package org.socialculture.platform.performance.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterRequest;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterResponse;
import org.socialculture.platform.performance.dto.request.PerformanceUpdateRequest;
import org.socialculture.platform.performance.dto.response.PerformanceDetailResponse;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.dto.response.PerformanceUpdateResponse;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Override
    public PerformanceRegisterResponse registerPerformance(PerformanceRegisterRequest performanceRegisterRequest) {

        return null;
    }

    @Override
    public List<PerformanceListResponse> getPerformanceList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<PerformanceWithCategory> performanceList = performanceRepository.getPerformanceWithCategoryList(pageRequest);

        return performanceList.stream()
                .map(PerformanceListResponse::from)
                .toList();
    }

    @Override
    public PerformanceDetailResponse getPerformanceDetail(Long performanceId) {
        return performanceRepository.getPerformanceDetail(performanceId)
                .map(PerformanceDetailResponse::from)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
    }

    // Todo : 사용자 이메일과 같은지 확인 절차 추가할 것
    @Override
    @Transactional
    public PerformanceUpdateResponse updatePerformance(Long performanceId, PerformanceUpdateRequest performanceUpdateRequest) {
        PerformanceEntity performanceEntity = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));

        performanceEntity.updatePerformance(PerformanceUpdateRequest.toEntity(performanceUpdateRequest));
        return PerformanceUpdateResponse.from(performanceEntity.getPerformanceId());
    }
}
