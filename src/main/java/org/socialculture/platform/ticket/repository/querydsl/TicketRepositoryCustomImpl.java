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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
     * 이메일을 기반으로 멤버의 티켓 목록을 페이징 처리하여 정렬 옵션에 따라 반환합니다.
     *
     * @param email 티켓을 조회할 멤버의 이메일
     * @param pageable 페이징 구현체 전달
     * @return 멤버의 티켓 목록을 나타내는 TicketEntity 리스트
     */
    @Override
    public List<TicketEntity> getAllTicketsByEmailWithPageAndSortOption(String email, Pageable pageable) {
        QTicketEntity ticketEntity = QTicketEntity.ticketEntity;

        // 정렬 기준에 따라 OrderSpecifier를 결정
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), ticketEntity);

        return jpaQueryFactory.selectFrom(ticketEntity)
                .join(ticketEntity.performance, QPerformanceEntity.performanceEntity).fetchJoin()
                .where(ticketEntity.member.email.eq(email))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    /**
     * Sort 객체로부터 OrderSpecifiers 목록을 생성합니다.
     *
     * @param sort Pageable에서 전달된 Sort 객체
     * @param ticketEntity 정렬 대상이 되는 QTicketEntity 객체
     * @return OrderSpecifier 목록
     * @throws GeneralException 잘못된 sortOption(property)가 제공된 경우 발생
     */
    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, QTicketEntity ticketEntity) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            // 각 필드에 맞게 OrderSpecifier 추가
            if (property.equals("ticketId")) {
                orderSpecifiers.add(new OrderSpecifier<>(direction, ticketEntity.ticketId));
            } else if (property.equals("price")) {
                orderSpecifiers.add(new OrderSpecifier<>(direction, ticketEntity.price));
            } else if (property.equals("expired")) {
                orderSpecifiers.add(new OrderSpecifier<>(direction, ticketEntity.deletedAt));
            } else {
                throw new GeneralException(ErrorStatus._TICKET_INVALID_SORT_OPTION);
            }
        });

        return orderSpecifiers;
    }
}
