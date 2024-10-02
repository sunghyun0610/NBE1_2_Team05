package org.socialculture.platform.performance.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterRequest;
import org.socialculture.platform.performance.dto.response.PerformanceRegisterResponse;
import org.socialculture.platform.performance.dto.request.PerformanceUpdateRequest;
import org.socialculture.platform.performance.dto.response.PerformanceDetailResponse;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.dto.response.PerformanceUpdateResponse;
import org.socialculture.platform.performance.entity.CategoryEntity;
import org.socialculture.platform.performance.entity.PerformanceCategoryEntity;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.CategoryRepository;
import org.socialculture.platform.performance.repository.PerformanceCategoryRepository;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceCategoryRepository performanceCategoryRepository;
    private final CategoryRepository categoryRepository;

    //TODO : 사용자 넣기
    @Transactional
    @Override
    public PerformanceRegisterResponse registerPerformance(PerformanceRegisterRequest performanceRegisterRequest) {
        PerformanceEntity performanceEntity = performanceRegisterRequest.toEntity(performanceRegisterRequest);
        performanceEntity = performanceRepository.save(performanceEntity);

        performanceCategorySave(performanceEntity, performanceRegisterRequest.categories());
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

    private void performanceCategorySave(PerformanceEntity performanceEntity, List<String> categories) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByNameKrIn(categories); // 한 번에 조회

        if (categoryEntities.isEmpty()) {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        categoryEntities.forEach(e ->
                performanceCategoryRepository.save(PerformanceCategoryEntity.of(performanceEntity, e)));
    }
}
