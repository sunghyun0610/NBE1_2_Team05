package org.socialculture.platform.ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 티켓 데이터 컨트롤러
 *
 * @author ycjung
 */
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<List<TicketResponseDto>>> getAllTicketsByMemberId() {
        log.info("Get all tickets by member id");

        return ApiResponse.onSuccess(ticketService.getAllTicketsByMemberId());
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponseDto>> getTicketById(@PathVariable("ticketId") Long ticketId) {
        log.info("Get ticket by id: {}", ticketId);

        if (ticketId == null) {
            throw new GeneralException(ErrorStatus._TICKET_ID_MISSING);
        }
        return ApiResponse.onSuccess(ticketService.getTicketByMemberIdAndTicketId(ticketId));
    }


}
