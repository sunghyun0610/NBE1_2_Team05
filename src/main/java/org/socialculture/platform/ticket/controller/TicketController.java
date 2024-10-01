package org.socialculture.platform.ticket.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @param page
     * @param size
     * @param option
     * @param isAscending
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponseDto>>> getAllTicketsByMemberIdWithPageAndSortOptionDesc(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "option", defaultValue = "ticketId") String option,
            @RequestParam(value = "isAscending", defaultValue = "false") boolean isAscending) {
        log.info("Get tickets by member id with pagination and sort - Page: {}, Size: {}, Sort Option: {}, Ascending : {}", page, size, option, isAscending);

        // page와 size 값이 음수일 경우 예외 처리
        if (page < 0 || size < 0) {
            throw new GeneralException(ErrorStatus._TICKET_INVALID_PAGINATION_PARAMETERS);
        }

        return ApiResponse.onSuccess(ticketService.getAllTicketsByEmailWithPageAndSortOption(page, size, option, isAscending));
    }
}
