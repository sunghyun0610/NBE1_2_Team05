package org.socialculture.platform.comment.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.socialculture.platform.comment.controller.CommentController;

import org.socialculture.platform.comment.dto.request.CommentCreateRequest;
import org.socialculture.platform.comment.dto.response.CommentCreateResponse;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.service.CommentService;
import org.socialculture.platform.comment.entity.CommentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false) // Spring Security 필터 비활성화 (CSRF 포함)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("전체 댓글 조회 테스트")
    @WithMockUser
        // 인증된 사용자로 테스트 수행
    void testGetComment() throws Exception {
        // given : 테스트 전 준비 단계 , 필요한 데이터 세팅 및 환경 구성
        CommentReadDto comment1 = CommentReadDto.builder()
                .commentId(1L)
                .memberId(1L)
                .content("Great performance!")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parentId(null) // 최상위 댓글일 경우 null
                .commentStatus(CommentStatus.ACTIVE)
                .build();//첫번째 댓글

        CommentReadDto comment2 = CommentReadDto.builder()
                .commentId(2L)
                .memberId(1L)
                .content("Amazing show!")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .parentId(1L) // 부모 댓글의 ID 설정
                .commentStatus(CommentStatus.ACTIVE)
                .build();//대댓긋

        // 페이지 정보 설정
        int page = 0; // 페이지 인덱스 (0부터 시작)
        int size = 10; // 한 페이지에 들어갈 데이터 수

// Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        List<CommentReadDto> commentList = List.of(comment1, comment2);

        Mockito.when(commentService.getAllComment(anyLong(), any(Pageable.class)))
                .thenReturn(commentList);//실제 내가만든 서비스로직을 타는게 아님. 미리정의한 결과인 commentList 반환함.

        // when : 실제 테스트 동작
        ResultActions resultActions = mockMvc.perform(get("/api/v1/comments/1")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())//응답상태 검증
                .andExpect(jsonPath("$.result", hasSize(2))) // 댓글이 2개 반환되었는지 확인
                .andExpect(jsonPath("$.result[0].content").value("Great performance!"))//댓글의 내용 확인
                .andExpect(jsonPath("$.result[1].content").value("Amazing show!"))
                .andExpect(jsonPath("$.result[0].memberId").value(1L))
                .andExpect(jsonPath("$.result[1].memberId").value(1L))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 생성 테스트")
    @WithMockUser
    void testCreateComment() throws Exception{
        //given
        CommentCreateRequest commentCreateRequest= CommentCreateRequest.builder()
                .content("댓글 생성 테스트코드입니다.")
                .parentId(1L)
                .build();

        CommentCreateResponse commentCreateResponse=CommentCreateResponse.of(1L,
                "댓글 생성 테스트코드입니다.",1L);

        //아무 createComment호출될떄 commentCreateResponse반환하도록설정
        Mockito.when(commentService.createComment(anyLong(),anyLong(),any(CommentCreateRequest.class)))
                .thenReturn(commentCreateResponse);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentCreateRequest)));
        
        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.result.commentId").value(1L))
                .andExpect(jsonPath("$.result.content").value("댓글 생성 테스트코드입니다."))
                .andExpect(jsonPath("$.result.performanceId").value(1L))
                .andDo(print());

    }
}
