package org.socialculture.platform.ticket.repository.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.performance.entity.QPerformanceEntity;
import org.socialculture.platform.ticket.entity.QTicketEntity;
import org.socialculture.platform.ticket.entity.TicketEntity;

import java.util.List;
import java.util.Objects;

/**
 * QueryDSL 을 사용하기 위한 repo impl
 *
 * @author ycjung
 */
@RequiredArgsConstructor
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * fetchJoin 을 통한 나의 티켓 전체 조회 - 페이징 + 소트
     * @param email
     * @param offset
     * @param pageSize
     * @param sortOption
     * @return
     */
    @Override
    public List<TicketEntity> getAllTicketsByEmailWithPageAndSortOption(String email, long offset, int pageSize, String sortOption, boolean isAscending) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;
        OrderSpecifier<?> orderSpecifier;

        // 정렬 순서 설정 (isAscending 값에 따라 결정)
        Order order = isAscending ? Order.ASC : Order.DESC;

        // sortOption 에 따른 분기 처리
        if (Objects.isNull(sortOption) || sortOption.equals("ticketId")) { // sortOption 이 null 일 경우 ticketId 기준 정렬
            orderSpecifier = new OrderSpecifier<>(order, ticketEntity.ticketId);
        } else if (sortOption.equals("price")) { // sortOption 이 'price'일 경우 price 기준 정렬
            orderSpecifier = new OrderSpecifier<>(order, ticketEntity.price);
        } else if (sortOption.equals("expired")) { // sortOption 이 'expired'일 경우 deletedAt 기준 정렬
            orderSpecifier = new OrderSpecifier<>(order, ticketEntity.deletedAt);
        } else {
            throw new GeneralException(ErrorStatus._TICKET_INVALID_SORT_OPTION);
        }

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .where(ticketEntity.member.email.eq(email))
                .offset(offset) // 조회하려고 하는 페이지가 과거일수록 느려진다.
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
    }
}
