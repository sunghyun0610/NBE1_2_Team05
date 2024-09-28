package org.socialculture.platform.ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.ticket.dto.response.TicketResponse;
import org.socialculture.platform.ticket.service.TicketService;
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
    public List<TicketResponse> getAllTicketsByMemberId() {
        log.info("Get all tickets by member id");

        return ticketService.getAllTicketsByMemberId();
    }

    @GetMapping("/tickets/{ticketId}")
    public TicketResponse getTicketById(@PathVariable("ticketId") Long ticketId) {
        log.info("Get ticket by id: {}", ticketId);

        return ticketService.getTicketByMemberIdAndTicketId(ticketId);
    }


}
