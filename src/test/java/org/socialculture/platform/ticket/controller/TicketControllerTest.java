package org.socialculture.platform.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.socialculture.platform.ticket.dto.request.TicketRequestDto;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Ticket 컨트롤러 테스트
 *
 * @author ycjung
 */
@WebMvcTest(controllers = TicketController.class)  // TicketController를 대상으로 WebMvcTest를 수행 (컨트롤러 단위 테스트)
@ExtendWith(SpringExtension.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    @DisplayName("티켓 컨트롤러 조회 테스트")
    @WithMockUser(username = "user@example.com", roles = "LOCAL")
    void getTickets() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 테스트에 사용할 가짜 티켓 응답 데이터를 생성
        List<TicketResponseDto> mockTicketResponsDtos = List.of(
                TicketResponseDto.create(1L, 1L, "꿈의 교향곡", LocalDateTime.now(), 2, 100, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)),
                TicketResponseDto.create(2L, 2L,"영원의 메아리", LocalDateTime.now(), 3, 150, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2))
        );

        given(ticketService.getAllTicketsByEmailWithPageAndSortOption(eq(username), anyInt(), anyInt(), isNull(), anyBoolean()))
                .willReturn(mockTicketResponsDtos);

        ResultActions result = this.mockMvc.perform(
                get("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "3")
        );

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("티켓 컨트롤러 상세 조회 테스트")
    @WithMockUser(username = "user@example.com", roles = "LOCAL")
    void getTicketDetail() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 가짜 티켓 응답 DTO 생성
        TicketResponseDto mockTicketResponse = TicketResponseDto.create(
                1L,
                2L,
                "꿈의 교향곡",
                LocalDateTime.now(),
                2,
                100,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        given(ticketService.getTicketByEmailAndTicketId(eq(username), eq(1L)))
                .willReturn(mockTicketResponse);

        // 실제 요청을 보내는 부분
        ResultActions result = this.mockMvc.perform(
                get("/api/v1/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("티켓 컨트롤러 발권 테스트")
    @WithMockUser(username = "user@example.com", roles = "LOCAL")
    void buyTicket() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 가짜 티켓 요청 DTO 생성
        TicketRequestDto mockTicketRequest = TicketRequestDto.create(
                1L,
                2,
                1L
        );
        // 가짜 티켓 응답 DTO 생성
        TicketResponseDto mockTicketResponse = TicketResponseDto.create(
                1L,
                2L,
                "꿈의 교향곡",
                LocalDateTime.now(),
                2,
                90000,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        given(ticketService.registerTicket(username, mockTicketRequest))
                .willReturn(mockTicketResponse);

        // 실제 요청을 보내는 부분
        ResultActions result = this.mockMvc.perform(
                post("/api/v1/tickets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockTicketRequest)) // content 로 JSON 요청 바디 추가
        );

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("티켓 컨트롤러 티켓 취소 테스트")
    @WithMockUser(username = "user@example.com", roles = "LOCAL")
    void cancelTicket() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long ticketId = 1L; // 가짜 티켓 ID

        // 티켓 취소 서비스 호출 가짜 응답 설정
        willDoNothing().given(ticketService).deleteTicket(username, ticketId);

        // 실제 요청을 보내는 부분
        ResultActions result = this.mockMvc.perform(
                delete("/api/v1/tickets/{ticketId}", ticketId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // 응답 상태 코드가 200 OK 인지 확인
        result.andExpect(status().isOk());
    }

    // 객체를 JSON 문자열로 변환하는 헬퍼 메서드
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
