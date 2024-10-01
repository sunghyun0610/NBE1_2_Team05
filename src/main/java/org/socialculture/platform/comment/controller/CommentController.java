package org.socialculture.platform.comment.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    /**
     * 전체 댓글 조회
     * @author sunghyun0610
     * @param performanceId
     * @return 공통 Response사용하여 CommentReadDto리스트 반환
     *
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<List<CommentReadDto>>> getComment(@PathVariable long performanceId) {
        List<CommentReadDto> commentReadDtos = commentService.getAllComment(performanceId);
        return ApiResponse.onSuccess(commentReadDtos);
    }


    /**
     * 댓글 생성
     * @author sunghyun0610
     * @param performanceId
     * @return 공통 Response사용하여 commentCreateResponse 반환
     *
     */
    @PostMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(@PathVariable long performanceId, @RequestBody CommentCreateRequest commentCreateRequest) {
        System.out.println("----before service");
        CommentCreateResponse commentCreateResponse = commentService.createComment(performanceId, commentCreateRequest);

            return ApiResponse.onSuccess(HttpStatus.CREATED, "COMMNET201", "댓글 작성 성공", commentCreateResponse);


    }

    /**
     * 댓글 수정
     * @author sunghyun0610
     * @param commentId
     * @return 공통 Response사용하여 commentUpdateResponse 반환
     *
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(@PathVariable long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest){
        //commentId를 통해서 performanceId를 가져와야함. -> 반환해줘야함
        CommentUpdateResponse commentUpdateResponse = commentService.updateComment(commentId,commentUpdateRequest);
        return ApiResponse.onSuccess(commentUpdateResponse);
    }

    /**
     * 댓글 삭제
     * @author sunghyun0610
     * @param commentId
     * @return 공통 Response사용하여 commentDeleteResponse 반환
     *
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteResponse>> deleteComment(@PathVariable long commentId){
        CommentDeleteResponse commentDeleteResponse = commentService.deleteComment(commentId);
        return ApiResponse.onSuccess(commentDeleteResponse);

    }
}
