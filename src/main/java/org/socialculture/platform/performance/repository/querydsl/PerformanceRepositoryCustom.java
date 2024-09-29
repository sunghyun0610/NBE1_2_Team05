package org.socialculture.platform.performance.repository.querydsl;

import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceRepositoryCustom {
    List<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable);
}
