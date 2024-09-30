package org.socialculture.platform.ticket.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.ticket.entity.QPerformanceEntity;
import org.socialculture.platform.ticket.entity.QTicketEntity;
import org.socialculture.platform.ticket.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * QueryDSL 을 사용하기 위한 repo impl
 *
 * @author ycjung
 */
@Repository
@RequiredArgsConstructor
public class DslTicketRepositoryImpl implements DslTicketRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * fetchJoin 을 통한 나의 티켓 전체 조회
     * @param memberId
     * @return
     */
    @Override
    public List<TicketEntity> getAllTicketsByMemberId(long memberId) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin() // join fetch 사용
                .where(ticketEntity.member.memberId.eq(memberId))
                .fetch();
    }

    @Override
    public List<TicketEntity> getAllTicketsByMemberIdWithPage(long memberId, long offset, int pageSize) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .offset(offset) // 조회하려고 하는 페이지가 과거일수록 느려진다.
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<TicketEntity> getAllTicketsByMemberIdWithPageAndSortOptionDesc(long memberId, long offset, int pageSize, String sortOption) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;
        OrderSpecifier<?> orderSpecifier;

        if (Objects.isNull(sortOption)) {
            orderSpecifier = new OrderSpecifier<>(Order.DESC, ticketEntity.ticketId);
        } else if (sortOption.equals("price")) { // 가격 기준 내림차순
            orderSpecifier = new OrderSpecifier<>(Order.DESC, ticketEntity.price);
        } else if (sortOption.equals("expired")) { // 만료 기준 내림차순
            orderSpecifier = new OrderSpecifier<>(Order.DESC, ticketEntity.deletedAt);
        } else {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .offset(offset) // 조회하려고 하는 페이지가 과거일수록 느려진다.
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
