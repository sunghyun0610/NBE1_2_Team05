package org.socialculture.platform.performance.repository.querydsl;

import org.socialculture.platform.performance.dto.domain.PerformanceDetail;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PerformanceRepositoryCustom {
    Page<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable, Long categoryId, String search);

    Optional<PerformanceDetail> getPerformanceDetail(Long performanceId);

    Page<PerformanceWithCategory> getMyPerformanceWithCategoryList(String email, Pageable pageable);
}
