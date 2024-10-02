package org.socialculture.platform.performance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "performance_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_category_id")
    private Long performanceCategoryId;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private PerformanceEntity performance;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private PerformanceCategoryEntity(PerformanceEntity performance, CategoryEntity category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.performance = performance;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PerformanceCategoryEntity of(PerformanceEntity performanceEntity, CategoryEntity categoryEntity) {
        return new PerformanceCategoryEntity(performanceEntity, categoryEntity, LocalDateTime.now(), LocalDateTime.now());
    }
}
