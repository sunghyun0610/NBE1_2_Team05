package org.socialculture.platform.performance.repository;

import jakarta.persistence.LockModeType;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.querydsl.PerformanceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<PerformanceEntity, Long>, PerformanceRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM  performance p WHERE p.performanceId= :performanceId")
    Optional<PerformanceEntity> findByIdWithLock(@Param("performanceId") Long performanceId);

}
