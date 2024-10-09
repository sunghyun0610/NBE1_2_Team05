package org.socialculture.platform.ticket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.ticket.dto.request.TicketRequestDto;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 티켓 데이터 컨트롤러
 *
 * @author ycjung
 */
@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final TicketService ticketService;

    /**
     * 나의 티켓 전체 조회 - 페이징 처리
     *
     * @param page
     * @param size
     * @param option
     * @param isAscending
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponseDto>>> getAllTicketsByEmailWithPageAndSortOption(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "option", defaultValue = "ticketId") String option,
            @RequestParam(value = "isAscending", defaultValue = "false") boolean isAscending) {
        log.info("Get tickets by member id with pagination and sort - Page: {}, Size: {}, Sort Option: {}, Ascending : {}", page, size, option, isAscending);

        // page와 size 값이 음수일 경우 예외 처리
        if (page < 0 || size < 0) {
            throw new GeneralException(ErrorStatus._TICKET_INVALID_PAGINATION_PARAMETERS);
        }

        return ApiResponse.onSuccess(ticketService.getAllTicketsByEmailWithPageAndSortOption(userDetails.getUsername(), page, size, option, isAscending));
    }

    /**
     * 나의 티켓 상세 조회
     *
     * @param ticketId
     * @return
     */
    @GetMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<TicketResponseDto>> getTicketById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable("ticketId") Long ticketId) {
        log.info("Get ticket detail by id: {}", ticketId);

        if (Objects.isNull(ticketId)) {
            throw new GeneralException(ErrorStatus._TICKET_ID_MISSING);
        }
        return ApiResponse.onSuccess(ticketService.getTicketByEmailAndTicketId(userDetails.getUsername(), ticketId));
    }

    /**
     * 티켓 발권
     * 
     * @param userDetails
     * @param ticketRequestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponseDto>> buyTicket(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody TicketRequestDto ticketRequestDto) {
        log.info("Buy ticket: {}", ticketRequestDto);

        return ApiResponse.onSuccess(ticketService.registerTicket(userDetails.getUsername(), ticketRequestDto));
    }

    /**
     * 티켓 취소
     * 
     * @param userDetails
     * @param ticketId
     * @return
     */
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ApiResponse<Void>> cancelTicket(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable("ticketId") Long ticketId) {
        log.info("Cancel ticket by id: {}", ticketId);

        ticketService.deleteTicket(userDetails.getUsername(), ticketId);

        return ApiResponse.onSuccess();
    }
}
