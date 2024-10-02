package org.socialculture.platform.ticket.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<TicketResponseDto> getAllTicketsByEmailWithPageAndSortOption(int page, int size, String sortOption, boolean isAscending) {
        // Pageable 객체 생성 (정렬은 sortOption과 isAscending에 기반하여 설정)
        Sort sort = isAscending ? Sort.by(sortOption).ascending() : Sort.by(sortOption).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ticketRepository.getAllTicketsByEmailWithPageAndSortOption(MEMBER_EMAIL, pageable)
                .stream()
                .map(TicketResponseDto::from)
                .collect(Collectors.toList());
    }
}
