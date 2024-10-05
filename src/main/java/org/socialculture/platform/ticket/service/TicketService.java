package org.socialculture.platform.ticket.service;

import org.socialculture.platform.ticket.dto.request.TicketRequestDto;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;

import java.util.List;

/**
 * 티켓 서비스 인터페이스
 *
 * @author ycjung
 */
public interface TicketService {
    // 전체 조회
    List<TicketResponseDto> getAllTicketsByEmailWithPageAndSortOption(int page, int size, String sortOption, boolean isAscending);

    // 상세 조회
    TicketResponseDto getTicketByEmailAndTicketId(Long ticketId);

    TicketResponseDto registerTicket(TicketRequestDto ticketRequest);

    // TicketResponse updateTicket(Long id, TicketRequest ticketRequest);

    // void deleteTicket(Long id);
}
