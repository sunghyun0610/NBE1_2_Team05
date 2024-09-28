package org.socialculture.platform.ticket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.ticket.dto.response.TicketResponse;
import org.socialculture.platform.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Ticket 컨트롤러 테스트
 *
 * @author ycjung
 */
@WebMvcTest(controllers = TicketController.class)  // TicketController를 대상으로 WebMvcTest를 수행 (컨트롤러 단위 테스트)
@MockBean(JpaMetamodelMappingContext.class)  // JPA 메타모델 문제 해결을 위한 MockBean 설정 (테스트 환경에서 메타모델을 Mock으로 사용)
@AutoConfigureMockMvc(addFilters = false)  // Spring Security 필터를 비활성화
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc를 통해 HTTP 요청과 응답 테스트 수행 (Spring MVC의 동작을 모의)

    @MockBean
    private TicketService ticketService;  // TicketService를 Mock으로 주입하여 실제 구현체 대신 사용

    @BeforeEach
    void setUp(final WebApplicationContext context) {
        // MockMvc를 WebApplicationContext로 설정하여 테스트할 때 항상 UTF-8 인코딩을 사용
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())  // 모든 테스트 수행 시 결과를 출력
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // UTF-8 인코딩 필터 추가
                .build();
    }

    @Test
    @DisplayName("티켓 컨트롤러 조회 테스트")
    void getTickets() throws Exception {
        // 테스트에 사용할 가짜 티켓 응답 데이터를 생성
        List<TicketResponse> mockTicketResponses = List.of(
                TicketResponse.of(1L, "꿈의 교향곡", LocalDateTime.now(), 2, 100, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)),
                TicketResponse.of(2L, "영원의 메아리", LocalDateTime.now(), 3, 150, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2))
        );

        // Mock TicketService의 getAllTickets 메서드가 호출되었을 때, 가짜 응답 데이터를 반환하도록 설정
        when(ticketService.getAllTicketsByMemberId()).thenReturn(mockTicketResponses);

        // MockMvc를 통해 "/api/v1/ticket" 경로에 GET 요청을 보낼 준비
        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/tickets")
                //.header("", "")  // (필요한 경우) 요청에 헤더를 추가할 수 있음
                //.param("", "")  // (필요한 경우) 요청에 추가적인 파라미터를 설정할 수 있음
                .with(csrf())  // (필요한 경우) CSRF 보호를 우회하는 설정
                .contentType(MediaType.APPLICATION_JSON);  // 요청의 Content-Type을 JSON으로 설정

        // MockMvc를 사용하여 요청을 수행하고, 그 결과를 받음
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        // 응답 상태가 200 OK인지 검증
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("티켓 조회 실패 - 티켓이 없는 경우")
    void getTickets_NoTickets() throws Exception {
        // 빈 응답을 반환하도록 Mock 설정
        when(ticketService.getAllTicketsByMemberId()).thenReturn(Collections.emptyList());

        // MockMvc를 통해 "/api/v1/tickets" 경로에 GET 요청을 보냄
        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON);

        // 응답이 200 OK 이고, 비어 있는 목록이 반환되는지 검증
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));  // 빈 배열 응답 확인
    }

    @Test
    @DisplayName("티켓 조회 실패 - 서비스 예외 발생")
    void getTickets_ServiceException() throws Exception {
        // 서비스에서 예외 발생하도록 설정
        when(ticketService.getAllTicketsByMemberId()).thenThrow(new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR));

        // MockMvc를 통해 "/api/v1/tickets" 경로에 GET 요청을 보냄
        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON);

        // 응답이 500 Internal Server Error인지 검증
        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError());
    }
}
