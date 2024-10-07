package org.socialculture.platform.performance.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.performance.dto.CategoryDto;
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

import static org.socialculture.platform.global.apiResponse.exception.ErrorStatus.PERFORMANCE_NOT_FOUND;

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

        List<CategoryEntity> categoryEntities = performanceCategorySave(performanceEntity, performanceRegisterRequest.categories());
        List<CategoryDto> categoryDtos = categoryEntities.stream()
                .map(CategoryDto::toDto).toList();

        return PerformanceRegisterResponse.of(performanceEntity, categoryDtos);
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
                .orElseThrow(() -> new GeneralException(PERFORMANCE_NOT_FOUND));
    }

    // Todo : 사용자 이메일과 같은지 확인 절차 추가할 것
    @Override
    @Transactional
    public PerformanceUpdateResponse updatePerformance(Long performanceId, PerformanceUpdateRequest performanceUpdateRequest) {
        PerformanceEntity performanceEntity = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new GeneralException(PERFORMANCE_NOT_FOUND));

        performanceEntity.updatePerformance(PerformanceUpdateRequest.toEntity(performanceUpdateRequest));
        return PerformanceUpdateResponse.from(performanceEntity.getPerformanceId());
    }

    @Override
    @Transactional
    public void deletePerformance(Long performanceId) {
        PerformanceEntity performanceEntity = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));

        performanceEntity.updateDeleteAt();
    }

    private List<CategoryEntity> performanceCategorySave(PerformanceEntity performanceEntity, List<String> categories) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByNameKrIn(categories); // 한 번에 조회

        if (categoryEntities.isEmpty()) {
            throw new GeneralException(ErrorStatus.CATEGORY_NOT_FOUND);
        }

        categoryEntities.forEach(e ->
                performanceCategoryRepository.save(PerformanceCategoryEntity.of(performanceEntity, e)));

        return categoryEntities;
    }

    @Override
    public List<PerformanceListResponse> getMyPerformanceList(String email, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<PerformanceWithCategory> performanceList = performanceRepository.getMyPerformanceWithCategoryList(email, pageRequest);

        if (performanceList.isEmpty()) {
            throw new GeneralException(PERFORMANCE_NOT_FOUND);
        }

        return performanceList.stream()
                .map(PerformanceListResponse::from)
                .toList();
    }
}
