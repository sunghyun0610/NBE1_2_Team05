package org.socialculture.platform.ticket.dto.response;

import lombok.Builder;
import org.socialculture.platform.ticket.entity.TicketEntity;

import java.time.LocalDateTime;

/**
 * Ticket 에 대한 Response 정보 매핑
 *
 * @author ycjung
 */
@Builder
public record TicketResponseDto(
        Long ticketId,
        String performanceTitle,
        LocalDateTime dateTime, // 티켓예매시간
        int quantity, // 예매인원
        int price, //티켓 총 가격
        LocalDateTime dateStartTime,
        LocalDateTime dateEndTime

) {
    // 정적 팩토리 메서드 create
    public static TicketResponseDto create(Long ticketId, String performanceTitle, LocalDateTime dateTime,
                                           int quantity, int price, LocalDateTime dateStartTime, LocalDateTime dateEndTime) {
        return TicketResponseDto.builder()
                .ticketId(ticketId)
                .performanceTitle(performanceTitle)
                .dateTime(dateTime)
                .quantity(quantity)
                .price(price)
                .dateStartTime(dateStartTime)
                .dateEndTime(dateEndTime)
                .build();
    }

    // 엔티티 로 부터 DTO 생성하는 메서드 from
    public static TicketResponseDto fromEntity(TicketEntity ticketEntity) {
        return new TicketResponseDto(
                ticketEntity.getTicketId(),
                ticketEntity.getPerformance().getTitle(),
                ticketEntity.getDateTime(),
                ticketEntity.getQuantity(),
                ticketEntity.getPrice(),
                ticketEntity.getCreatedAt(),
                ticketEntity.getDeletedAt()
        );
    }
}