package org.socialculture.platform.ticket.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 티켓 서비스 구현체
 *
 * @author ycjung
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private static String MEMBER_EMAIL = "ello@test.com"; // 임시 메일 테스트 -> 토큰 발행되면 수정

    @Override
    public List<TicketResponseDto> getAllTicketsByEmailWithPageAndSortOptionDesc(int page, int size, String sortOption) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return ticketRepository.getAllTicketsByEmailWithPageAndSortOptionDesc(MEMBER_EMAIL,
                        pageRequest.getOffset(),
                        pageRequest.getPageSize(),
                        sortOption)
                .stream()
                .map(TicketResponseDto::from)
                .collect(Collectors.toList());
    }
}
