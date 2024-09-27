package org.socialculture.platform.performance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.socialculture.platform.util.ApiDocumentUtils.getDocumentRequest;
import static org.socialculture.platform.util.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PerformanceController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class PerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerformanceService performanceService;

    @Test
    @DisplayName("공연 전체 조회 테스트")
    @WithMockUser
    void getPerformanceListTest() throws Exception {
        //given
        List<PerformanceListResponse> performanceList = List.of(
                PerformanceListResponse.builder()
                        .memberName("홍길동")
                        .performanceId(1L)
                        .title("제목111")
                        .dateStartTime(LocalDateTime.now())
                        .dateEndTime(LocalDateTime.now().plusHours(4L))
                        .address("주소111111")
                        .imageUrl("이미지 주소")
                        .price(10000)
                        .status("NOT_CONFIRMED")
                        .category(List.of("MUSIC", "THEATER"))
                        .build()
                ,
                PerformanceListResponse.builder()
                        .memberName("홍길동222")
                        .performanceId(1L)
                        .title("제목122211")
                        .dateStartTime(LocalDateTime.now())
                        .dateEndTime(LocalDateTime.now().plusHours(5L))
                        .address("주소114231111")
                        .imageUrl("이미지 주소")
                        .price(20000)
                        .status("CONFIRMED")
                        .category(List.of("STANDUP", "DANCE"))
                        .build()
        );

        given(performanceService.getPerformanceList())
                .willReturn(performanceList);

        ResultActions result = this.mockMvc.perform(
                get("/api/v1/performance")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("performance-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[].memberName").type(JsonFieldType.STRING).description("주최자 이름"),
                                fieldWithPath("[].performanceId").type(JsonFieldType.NUMBER).description("공연 ID"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("공연 제목"),
                                fieldWithPath("[].dateStartTime").type(JsonFieldType.STRING).description("공연 시작시간"),
                                fieldWithPath("[].dateEndTime").type(JsonFieldType.STRING).description("공연 종료시간"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("공연장 주소"),
                                fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("공연 이미지 URL"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("티켓 가격"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING).description("공연 상태"),
                                fieldWithPath("[].category").type(JsonFieldType.ARRAY).description("공연 카테고리")

                        )
                ));
    }
}
