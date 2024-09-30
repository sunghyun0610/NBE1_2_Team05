package org.socialculture.platform.performance.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.socialculture.platform.member.entity.QMember;
import org.socialculture.platform.performance.dto.CategoryDTO;
import org.socialculture.platform.performance.dto.PerformanceDetail;
import org.socialculture.platform.performance.dto.PerformanceWithCategory;
import org.socialculture.platform.performance.entity.PerformanceStatus;
import org.socialculture.platform.performance.entity.QCategoryEntity;
import org.socialculture.platform.performance.entity.QPerformanceCategoryEntity;
import org.socialculture.platform.performance.entity.QPerformanceEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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

    public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }

    private BooleanBuilder performanceStatusNe(PerformanceStatus status) {
        return nullSafeBuilder(() -> qPerformanceEntity.performanceStatus.eq(status));
    }

    private BooleanBuilder performanceIdEq(Long performanceId) {
        return nullSafeBuilder(() -> qPerformanceEntity.performanceId.eq(performanceId));
    }

    private BooleanBuilder performanceCategoryEq(Long performanceId) {
        return nullSafeBuilder(() -> qPerformanceCategoryEntity.performance.performanceId.eq(performanceId));
    }

    /**
     * 공연 리스트 조회
     * @author Icecoff22
     * @param pageable
     * @return PerformanceWithCategory 형태의 리스트
     *
     */
    @Override
    public List<PerformanceWithCategory> getPerformanceWithCategoryList(Pageable pageable) {

        List<PerformanceWithCategory> performances = jpaQueryFactory.select(Projections.constructor(PerformanceWithCategory.class,
                        qMember.name.as("memberName"),
                        qPerformanceEntity.performanceId.as("performanceId"),
                        qPerformanceEntity.title.as("title"),
                        qPerformanceEntity.dateStartTime.as("dateStartTime"),
                        qPerformanceEntity.dateEndTime.as("dateEndTime"),
                        qPerformanceEntity.address.as("address"),
                        qPerformanceEntity.imageUrl.as("imageUrl"),
                        qPerformanceEntity.price.as("price"),
                        qPerformanceEntity.performanceStatus.as("status")
                ))
                .from(qPerformanceEntity)
                .leftJoin(qMember)
                .where(performanceStatusNe(NOT_CONFIRMED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return addCategoriesToPerformances(performances);
    }

    private List<PerformanceWithCategory> addCategoriesToPerformances(List<PerformanceWithCategory> performances) {
        for (PerformanceWithCategory performance : performances) {
            List<CategoryDTO> categories = getCategoriesByPerformance(performance.getPerformanceId());
            performance.updateCategories(categories);
        }
        return performances;
    }

    private List<CategoryDTO> getCategoriesByPerformance(Long performanceId) {
        // 조회된 공연 대상으로 각 공연당 카테고리 조회
        return jpaQueryFactory.select(Projections.constructor(CategoryDTO.class,
                        qCategoryEntity.categoryId,
                        qCategoryEntity.nameEn,
                        qCategoryEntity.nameKr))
                .from(qCategoryEntity)
                .join(qPerformanceCategoryEntity).on(qPerformanceCategoryEntity.category.eq(qCategoryEntity))
                .where(performanceCategoryEq(performanceId))
                .fetch();
    }

    /**
     * 공연 상세 조회
     * @author Icecoff22
     * @param performanceId
     * @return Optional PerformanceDetail dto
     *
     */
    @Override
    public Optional<PerformanceDetail> getPerformanceDetail(Long performanceId) {
        PerformanceDetail performanceDetail = jpaQueryFactory.select(Projections.constructor(PerformanceDetail.class,
                        qMember.name.as("memberName"),
                        qPerformanceEntity.performanceId.as("performanceId"),
                        qPerformanceEntity.title.as("title"),
                        qPerformanceEntity.dateStartTime.as("dateStartTime"),
                        qPerformanceEntity.dateEndTime.as("dateEndTime"),
                        qPerformanceEntity.description.as("description"),
                        qPerformanceEntity.maxAudience.as("maxAudience"),
                        qPerformanceEntity.address.as("address"),
                        qPerformanceEntity.imageUrl.as("imageUrl"),
                        qPerformanceEntity.price.as("price"),
                        qPerformanceEntity.remainingTickets.as("remainingTickets"),
                        qPerformanceEntity.startDate.as("startDate"),
                        qPerformanceEntity.performanceStatus.as("status"),
                        qPerformanceEntity.createdAt.as("createdAt"),
                        qPerformanceEntity.updatedAt.as("updatedAt")
                ))
                .from(qPerformanceEntity)
                .leftJoin(qMember)
                .where(performanceIdEq(performanceId))
                .fetchOne();

        if (performanceDetail != null) {
            List<CategoryDTO> categories = getCategoriesByPerformance(performanceDetail.getPerformanceId());
            performanceDetail.updateCategories(categories);
        }

        return Optional.ofNullable(performanceDetail);
    }
}
