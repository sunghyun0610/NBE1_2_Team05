package org.socialculture.platform.performance.repository.querydsl;

import org.socialculture.platform.performance.dto.domain.PerformanceDetail;
import org.socialculture.platform.performance.dto.domain.PerformanceWithCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

// 일단 이걸 지워야 오류가 없이 실행됨
public interface PerformanceRepositoryCustom {
    List<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable);

    Optional<PerformanceDetail> getPerformanceDetail(Long performanceId);
//    List<PerformanceWithoutCategory> getPerformanceWithCategoryList(Pageable pageable);
}
