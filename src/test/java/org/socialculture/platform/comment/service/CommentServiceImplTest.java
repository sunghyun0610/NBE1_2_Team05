package org.socialculture.platform.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.entity.CommentEntity;
import org.socialculture.platform.comment.repository.CommentRepository;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PerformanceRepository performanceRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //각 테스트가 실행되기 전에 MockitoAnnotations.openMocks(this)를 호출하여 @Mock으로 선언된 객체를 초기화합니다

    @Test
    @DisplayName("댓글 페이징 조회 서비스 성공 테스트")
    void getAllComment() {
        //Given
        long performanceId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // MemberEntity 생성
        MemberEntity member1 = MemberEntity.builder()
                .memberId(1L) // 멤버 ID 설정
                .name("testUser") // 추가적인 필드 설정 (예시)
                .email("test@example.com")
                .build();

// PerformanceEntity 생성
        PerformanceEntity performance1 = PerformanceEntity.builder()
                .performanceId(1L) // 공연 ID 설정
                .title("Amazing Performance") // 추가적인 필드 설정 (예시)
                .build();

        // MemberEntity 생성
        MemberEntity member2 = MemberEntity.builder()
                .memberId(2L) // 멤버 ID 설정
                .name("testUser2") // 추가적인 필드 설정 (예시)
                .email("test2@example.com")
                .build();

// PerformanceEntity 생성
        PerformanceEntity performance2 = PerformanceEntity.builder()
                .performanceId(2L) // 공연 ID 설정
                .title("Amazing Performance2") // 추가적인 필드 설정 (예시)
                .build();


        CommentEntity comment1 = CommentEntity.builder()
                .commentId(1L)
                .member(member1)
                .content("Great performance!")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .performance(performance1)
                .build();

        CommentEntity comment2 = CommentEntity.builder()
                .commentId(2L)
                .member(member2)
                .content("Amazing show!")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .performance(performance2)
                .build();

        List<CommentEntity> commentEntityList = List.of(comment1, comment2);

        when(commentRepository.findAllByPerformance_PerformanceId(performanceId, pageable))
                .thenReturn(commentEntityList);

        //When
        List<CommentReadDto> result = commentService.getAllComment(performanceId, page, size);

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Great performance!", result.get(0).getContent());
        assertEquals("Amazing show!", result.get(1).getContent());

        verify(commentRepository, times(1)).findAllByPerformance_PerformanceId(performanceId, pageable);

        /*
        *   반환된 결과가 null이 아님을 확인합니다 (assertNotNull(result)).
            결과 리스트의 크기가 예상대로 두 개임을 검증합니다 (assertEquals(2, result.size())).
            각 댓글의 내용이 예상한 값과 동일한지 확인합니다 (assertEquals).
            댓글 조회 메서드가 한 번 호출되었는지 검증합니다 (verify).
        * */

    }

    @Test
    @DisplayName("댓글이 없을 때. 실패 시 예외처리")
    void shouldThrowExceptionWhenCommentsNotFound() {
        // given
        long invalidPerformanceId = 999L; // Assume this ID does not exist
        int page = 0;
        int size = 10;

        Mockito.when(commentRepository.findAllByPerformance_PerformanceId(anyLong(), any(Pageable.class)))
                .thenReturn(Collections.emptyList());//알부로 빈 리스트 반환시킴

        // when & then
        GeneralException exception = assertThrows(GeneralException.class,
                () -> commentService.getAllComment(invalidPerformanceId, page, size));

        // ErrorStatus는 GeneralException의 메시지로 비교
        assertEquals(ErrorStatus.COMMENT_NOT_FOUND.getMessage(), "댓글 정보가 없습니다.");
    }

}