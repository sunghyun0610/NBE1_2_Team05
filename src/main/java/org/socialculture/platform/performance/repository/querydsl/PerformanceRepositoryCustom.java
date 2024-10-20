package org.socialculture.platform.performance.repository.querydsl;

import org.socialculture.platform.performance.dto.domain.PerformanceDetail;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepositoryCustom {
    Page<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable, Long categoryId, String search, String email);

    // 사용자 선호 카테고리 공연 추천 조회
    List<PerformanceWithCategory> getRecommendedPerformancesByMember(Long memberId);

    Optional<PerformanceDetail> getPerformanceDetail(Long performanceId);

    Page<PerformanceWithCategory> getMyPerformanceWithCategoryList(String email, Pageable pageable);
}
