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
import java.util.Optional;

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
    public List<TicketEntity> getAllTicketsByEmailWithPageAndSortOptionDesc(String email, long offset, int pageSize, String sortOption) {
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
                .where(ticketEntity.member.email.eq(email))
                .offset(offset) // 조회하려고 하는 페이지가 과거일수록 느려진다.
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
    }

    @Override
    public Optional<TicketEntity> getTicketByEmailAndTicketId(String email, Long ticketId) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

        TicketEntity ticket = jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .where(ticketEntity.ticketId.eq(ticketId)
                        .and(ticketEntity.member.email.eq(email)))
                .fetchOne();

        return Optional.ofNullable(ticket);
    }
}
