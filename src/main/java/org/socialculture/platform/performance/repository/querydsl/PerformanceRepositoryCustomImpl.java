package org.socialculture.platform.performance.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.socialculture.platform.member.entity.QMember;
import org.socialculture.platform.performance.dto.CategoryDTO;
import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.socialculture.platform.performance.entity.QCategoryEntity;
import org.socialculture.platform.performance.entity.QPerformanceCategoryEntity;
import org.socialculture.platform.performance.entity.QPerformanceEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.socialculture.platform.performance.entity.PerformanceStatus.NOT_CONFIRMED;

public class PerformanceRepositoryCustomImpl implements PerformanceRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public PerformanceRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QPerformanceEntity qPerformanceEntity = QPerformanceEntity.performanceEntity;
    QPerformanceCategoryEntity qPerformanceCategoryEntity = QPerformanceCategoryEntity.performanceCategoryEntity;
    QMember qMember = QMember.member;
    QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;

    /**
     * 공연 리스트 조회
     * @author 정승주
     * @param pageable
     * @return PerformanceWithCategory 형태의 리스트
     * @Discription
     */
    @Override
    public List<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable) {

        return jpaQueryFactory.select(Projections.constructor(PerformanceWithCategory.class,
                        qMember.name,
                qPerformanceEntity.performanceId,
                qPerformanceEntity.title,
                qPerformanceEntity.dateStartTime,
                qPerformanceEntity.dateEndTime,
                qPerformanceEntity.address,
                qPerformanceEntity.imageUrl,
                qPerformanceEntity.price,
                qPerformanceEntity.performanceStatus,
                        JPAExpressions.select(Projections.constructor(CategoryDTO.class,
                                qCategoryEntity.categoryId,
                                qCategoryEntity.nameEn,
                                qCategoryEntity.nameKr))
                                .from(qCategoryEntity)
                                .where(qPerformanceCategoryEntity.category.eq(qCategoryEntity))
                ))
                .from(qPerformanceEntity)
                .leftJoin(qMember).on(qMember.eq(qPerformanceEntity.member))
                .leftJoin(qPerformanceCategoryEntity).on(qPerformanceEntity.eq(qPerformanceCategoryEntity.performance))
                .leftJoin(qCategoryEntity).on(qCategoryEntity.eq(qPerformanceCategoryEntity.category))
                .where(qPerformanceEntity.performanceStatus.ne(NOT_CONFIRMED))
                .fetch();
    }
}
