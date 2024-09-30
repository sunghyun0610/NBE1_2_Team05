package org.socialculture.platform.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.entity.TicketEntity;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
public class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;  // 테스트할 대상 서비스

    @Mock
    private TicketRepository ticketRepository;  // Mock으로 TicketRepository 주입

    @Mock
    private MemberRepository memberRepository;  // Mock으로 MemberRepository 주입

    // 공통으로 사용할 샘플 데이터
    private PerformanceEntity performance;
    private MemberEntity member;
    private TicketEntity ticketEntity;
    private List<TicketEntity> ticketList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // PerformanceEntity 샘플 데이터 생성
        performance = PerformanceEntity.builder()
                .performanceId(1L)
                .title("꿈의 교향곡")
                .price(50000)
                .dateStartTime(LocalDateTime.of(2023, 9, 1, 19, 0))
                .dateEndTime(LocalDateTime.of(2023, 9, 1, 21, 0))
                .build();

        // MemberEntity 샘플 데이터 생성
        member = MemberEntity.builder()
                .memberId(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        // TicketEntity 샘플 데이터 생성
        ticketEntity = TicketEntity.builder()
                .ticketId(1L)
                .performance(performance)
                .member(member)
                .dateTime(LocalDateTime.now())
                .quantity(2)
                .price(100000)  // 공연 가격 * 수량
                .build();

        // 샘플 티켓 리스트 생성
        ticketList = List.of(ticketEntity);
    }

    @Test
    @DisplayName("모든 티켓 조회 성공 테스트")
    void getAllTickets_Success() {
        // Given (Mock 동작 설정)
        when(ticketRepository.findAllByMemberMemberId(member.getMemberId())).thenReturn(ticketList);

        // When (서비스 호출)
        List<TicketResponseDto> result = ticketService.getAllTicketsByMemberId();

        // Then (결과 검증)
        assertFalse(result.isEmpty());  // 결과가 비어있지 않은지 확인
        assertEquals(1, result.size());  // 결과 리스트 크기가 1인지 확인

        TicketResponseDto ticketResponseDto = result.get(0);
        assertEquals(ticketEntity.getTicketId(), ticketResponseDto.ticketId());  // 티켓 ID가 일치하는지 확인
        assertEquals(ticketEntity.getPerformance().getTitle(), ticketResponseDto.performanceTitle());  // 공연 제목이 일치하는지 확인
        assertEquals(ticketEntity.getPrice(), ticketResponseDto.price());  // 티켓 가격이 일치하는지 확인

        verify(ticketRepository, times(1)).findAllByMemberMemberId(member.getMemberId());  // findAll()이 정확히 한 번 호출되었는지 확인
    }

    @Test
    @DisplayName("모든 티켓 조회 - NullPointerException 발생 테스트")
    void getAllTickets_NullPointerException() {
        // Given (Mock 동작 설정)
        when(ticketRepository.findAllByMemberMemberId(member.getMemberId())).thenThrow(new NullPointerException("Null 에러"));

        // When & Then (예외 발생 여부 검증)
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            ticketService.getAllTicketsByMemberId();
        });

        // 예외 메시지가 ErrorStatus._INTERNAL_SERVER_ERROR의 메시지와 일치하는지 확인
        assertEquals(ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(), exception.getCode().getResponseWithHttpStatus().getMessage());

        verify(ticketRepository, times(1)).findAllByMemberMemberId(member.getMemberId());  // 메서드가 호출되었는지 확인
    }

    @Test
    @DisplayName("모든 티켓 조회 - DataAccessException 발생 테스트")
    void getAllTickets_DataAccessException() {
        // Given (Mock 동작 설정)
        when(ticketRepository.findAllByMemberMemberId(member.getMemberId())).thenThrow(new DataAccessException("데이터베이스 오류") {});

        // When & Then (예외 발생 여부 검증)
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            ticketService.getAllTicketsByMemberId();
        });

        // 예외 메시지가 ErrorStatus._INTERNAL_SERVER_ERROR의 메시지와 일치하는지 확인
        assertEquals(ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(), exception.getCode().getResponseWithHttpStatus().getMessage());

        verify(ticketRepository, times(1)).findAllByMemberMemberId(member.getMemberId());  // 메서드가 호출되었는지 확인
    }

    @Test
    @DisplayName("모든 티켓 조회 - 예상치 못한 오류 발생 테스트")
    void getAllTickets_UnexpectedException() {
        // Given (Mock 동작 설정)
        when(ticketRepository.findAllByMemberMemberId(member.getMemberId())).thenThrow(new RuntimeException("예상치 못한 오류"));

        // When & Then (예외 발생 여부 검증)
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            ticketService.getAllTicketsByMemberId();
        });

        // 예외 메시지가 ErrorStatus._INTERNAL_SERVER_ERROR의 메시지와 일치하는지 확인
        assertEquals(ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(), exception.getCode().getResponseWithHttpStatus().getMessage());

        verify(ticketRepository, times(1)).findAllByMemberMemberId(member.getMemberId());  // 메서드가 호출되었는지 확인
    }
}
