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
     * 이메일을 기반으로 멤버의 티켓 목록을 페이징 처리하여 정렬 옵션에 따라 반환합니다.
     *
     * @param email 티켓을 조회할 멤버의 이메일
     * @param offset 데이터셋에서 조회를 시작할 위치 (페이징 처리에 사용)
     * @param pageSize 한 페이지에서 가져올 최대 티켓 수
     * @param sortOption 티켓을 정렬할 기준 필드 (예: ticketId, price, expired)
     * @param isAscending true일 경우 오름차순, false일 경우 내림차순 정렬
     * @return 멤버의 티켓 목록을 나타내는 TicketEntity 리스트
     * @throws GeneralException 잘못된 sortOption이 제공된 경우 발생
     */
    @Override
    public List<TicketEntity> getAllTicketsByEmailWithPageAndSortOption(String email, long offset, int pageSize, String sortOption, boolean isAscending) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

        // 정렬 기준에 따라 OrderSpecifier를 결정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortOption, isAscending, ticketEntity);

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .where(ticketEntity.member.email.eq(email))
                .offset(offset)
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
                        /* TODO : 추 후에 나의 티켓 상세 조회 권한에 따른 리팩토링이 필요해보임.
                        *   임시로 member email 을 통해 where 절 처리 */
                        .and(ticketEntity.member.email.eq(email)))
                .fetchOne();

        return Optional.ofNullable(ticket);
    }

    /**
     * 정렬 옵션과 순서에 따라 OrderSpecifier를 생성합니다.
     *
     * @param sortOption 정렬할 기준 필드 (예: ticketId, price, expired)
     * @param isAscending true일 경우 오름차순, false일 경우 내림차순 정렬
     * @param ticketEntity 정렬 대상이 되는 QTicketEntity 객체
     * @return 정렬 기준을 나타내는 OrderSpecifier 객체
     * @throws GeneralException 잘못된 sortOption이 제공된 경우 발생
     */
    private OrderSpecifier<?> getOrderSpecifier(String sortOption, boolean isAscending, QTicketEntity ticketEntity) {
        // 정렬 순서 설정 (isAscending 값에 따라 결정)
        Order order = isAscending ? Order.ASC : Order.DESC;

        // sortOption에 따른 분기 처리
        if (Objects.isNull(sortOption) || sortOption.equals("ticketId")) {
            return new OrderSpecifier<>(order, ticketEntity.ticketId);
        } else if (sortOption.equals("price")) {
            return new OrderSpecifier<>(order, ticketEntity.price);
        } else if (sortOption.equals("expired")) {
            return new OrderSpecifier<>(order, ticketEntity.deletedAt);
        } else {
            throw new GeneralException(ErrorStatus._TICKET_INVALID_SORT_OPTION);
        }
    }
}
