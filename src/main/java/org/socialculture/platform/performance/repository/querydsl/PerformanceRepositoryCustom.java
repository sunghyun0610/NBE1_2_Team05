package org.socialculture.platform.performance.repository.querydsl;

import org.socialculture.platform.performance.dto.PerformanceDetail;
import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepositoryCustom {
    List<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable);

    Optional<PerformanceDetail> getPerformanceDetail(Long performanceId);
}
