package org.socialculture.platform.ticket.service;

import org.socialculture.platform.ticket.dto.response.TicketResponseDto;

import java.util.List;

/**
 * 티켓 서비스 인터페이스
 * 
 * @author ycjung
 */
public interface TicketService {

    List<TicketResponseDto> getAllTicketsByMemberId();

    TicketResponseDto getTicketByMemberIdAndTicketId(Long ticketId);

    // TicketResponse createTicket(Long memberId, TicketRequest ticketRequest);

    // TicketResponse updateTicket(Long id, TicketRequest ticketRequest);

    // void deleteTicket(Long id);
}
