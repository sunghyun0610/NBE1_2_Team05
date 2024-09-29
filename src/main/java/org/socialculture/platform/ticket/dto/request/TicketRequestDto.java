package org.socialculture.platform.ticket.dto.request;

import org.socialculture.platform.ticket.entity.MemberEntity;
import org.socialculture.platform.ticket.entity.PerformanceEntity;
import org.socialculture.platform.ticket.entity.TicketEntity;

/**
 * Ticket 에 대한 Request 정보 매핑
 *
 * @author ycjung
 */
public record TicketRequestDto(
        Long performanceId,
        int quantity,
        Long couponId
) {
    // 정적 팩토리 메서드 of
    public static TicketRequestDto of(Long performanceId, Integer quantity, Long couponId) {
        return new TicketRequestDto(performanceId, quantity, couponId);
    }

    // DTO 로 부터 엔티티 생성하는 메서드 toEntity
    public TicketEntity toEntity(PerformanceEntity performance, MemberEntity member) {
        return TicketEntity.builder()
                .performance(performance)
                .member(member)
                .quantity(quantity)
                .price(performance.getPrice() * quantity) // 공연비 * 인원
                .build();
    }
}
