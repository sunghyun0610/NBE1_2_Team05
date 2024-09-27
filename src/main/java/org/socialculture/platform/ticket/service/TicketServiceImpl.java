package org.socialculture.platform.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.exception.BadRequestException;
import org.socialculture.platform.ticket.dto.response.TicketResponse;
import org.socialculture.platform.ticket.entity.MemberEntity;
import org.socialculture.platform.ticket.entity.TicketEntity;
import org.socialculture.platform.ticket.exception.TicketExceptionCode;
import org.socialculture.platform.ticket.repository.MemberRepository;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 티켓 서비스 구현체
 *
 * @author ycjung
 */
@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    // private final PerformanceRepository performanceRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, MemberRepository memberRepository) {
        this.ticketRepository = ticketRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        try {
            // 데이터베이스에서 모든 티켓을 조회하고, TicketResponse로 변환
            return ticketRepository.findAll().stream()
                    .map(TicketResponse::from)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            // NullPointerException 발생 시 로그 기록
            log.error("NullPointerException 발생: {}", e.getMessage(), e);
            throw new RuntimeException("티켓 데이터를 변환하는 중에 문제가 발생했습니다.");
        } catch (DataAccessException e) {
            // 데이터베이스 접근 중 발생하는 예외 처리 및 로그 기록
            log.error("데이터베이스 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("데이터베이스와의 연결 중에 문제가 발생했습니다.");
        } catch (Exception e) {
            // 기타 예외 처리 및 로그 기록
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
        }
    }

    @Override
    public TicketResponse getTicketById(Long id) {
        Optional<TicketEntity> ticket = ticketRepository.findById(id);
        ticket = null;
        return ticket.map(TicketResponse::from)
                .orElseThrow(() -> new BadRequestException(TicketExceptionCode.INVALID_TICKET_ID));
    }

}
