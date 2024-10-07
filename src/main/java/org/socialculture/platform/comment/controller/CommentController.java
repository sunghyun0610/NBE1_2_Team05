package org.socialculture.platform.comment.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.socialculture.platform.comment.dto.request.CommentCreateRequest;
import org.socialculture.platform.comment.dto.request.CommentUpdateRequest;
import org.socialculture.platform.comment.dto.response.CommentCreateResponse;
import org.socialculture.platform.comment.dto.response.CommentDeleteResponse;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.dto.response.CommentUpdateResponse;
import org.socialculture.platform.comment.service.CommentService;
import org.socialculture.platform.comment.service.CommentServiceImpl;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

//    public CommentController(CommentService commentService) {
//        this.commentService = commentService;
//    }


    /**
     * 전체 댓글 조회
     *
     * @param performanceId
     * @return 공통 Response사용하여 CommentReadDto리스트 반환
     * @author sunghyun0610
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<List<CommentReadDto>>> getComment(@PathVariable("performanceId") long performanceId,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        //page는 인덱스이기 때문에 0번부터 시작
        Pageable pageable = PageRequest.of(page, size, Sort.by("commentId").ascending());
        List<CommentReadDto> commentReadDtos = commentService.getAllComment(performanceId, pageable);
        return ApiResponse.onSuccess(commentReadDtos);
    }


    /**
     * 댓글 생성
     *
     * @param performanceId
     * @return 공통 Response사용하여 commentCreateResponse 반환
     * @author sunghyun0610
     */
    @PostMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(@PathVariable("performanceId") long performanceId, @RequestBody CommentCreateRequest commentCreateRequest) {
        logger.info("서비스 호출 전 : {}", performanceId);
        //jwt에서 userId를 가져와서 service단에 넘겨줘야함.
        CommentCreateResponse commentCreateResponse = commentService.createComment(performanceId, commentCreateRequest);
        logger.info("Successfully created a comment for performanceId: {}", performanceId);

        return ApiResponse.onSuccess(HttpStatus.CREATED, "COMMNET201", "댓글 작성 성공", commentCreateResponse);


    }

    /**
     * 댓글 수정
     *
     * @param commentId
     * @return 공통 Response사용하여 commentUpdateResponse 반환
     * @author sunghyun0610
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(@PathVariable("commentId") long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest) {
        //commentId를 통해서 performanceId를 가져와야함. -> 반환해줘야함
        CommentUpdateResponse commentUpdateResponse = commentService.updateComment(commentId, commentUpdateRequest);
        return ApiResponse.onSuccess(commentUpdateResponse);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return 공통 Response사용하여 commentDeleteResponse 반환
     * @author sunghyun0610
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteResponse>> deleteComment(@PathVariable("commentId") long commentId) {
        CommentDeleteResponse commentDeleteResponse = commentService.deleteComment(commentId);
        return ApiResponse.onSuccess(commentDeleteResponse);

    }
}
