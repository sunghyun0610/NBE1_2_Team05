package org.socialculture.platform.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.entity.TicketEntity;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private static Long MEMBER_ID = 1L;

    public TicketServiceImpl(TicketRepository ticketRepository, MemberRepository memberRepository) {
        this.ticketRepository = ticketRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<TicketResponseDto> getAllTicketsByMemberId() {
        try {
            // 데이터베이스에서 모든 티켓을 조회하고, TicketResponse로 변환
            return ticketRepository.findAllByMemberMemberId(MEMBER_ID).stream()
                    .map(TicketResponseDto::from)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            log.error("티켓 데이터 변환 문제 발생 : {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        } catch (DataAccessException e) {
            log.error("데이터베이스 오류 발생: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public TicketResponseDto getTicketByMemberIdAndTicketId(Long ticketId) {
        TicketEntity ticketEntity = ticketRepository.findByMemberMemberIdAndTicketId(MEMBER_ID, ticketId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._TICKET_NOT_FOUND));

        return TicketResponseDto.from(ticketEntity);
    }

}
