package org.socialculture.platform.performance.repository;

import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<PerformanceEntity, Long> {
}
