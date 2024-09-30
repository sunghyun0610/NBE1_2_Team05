package org.socialculture.platform.comment.controller;

import org.socialculture.platform.comment.dto.request.CommentCreateRequest;
import org.socialculture.platform.comment.dto.request.CommentUpdateRequest;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.dto.response.CommentUpdateResponse;
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
public class CommentController {

    private CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/comments/{performanceId}")
    public ResponseEntity<ApiResponse<List<CommentReadDto>>> getComment(@PathVariable long performanceId) {
        List<CommentReadDto> commentReadDtos = commentService.getAllComment(performanceId);
        return ApiResponse.onSuccess(commentReadDtos);
    }

    @PostMapping("/comments/{performanceId}")
    public ResponseEntity<ApiResponse<Void>> createComment(@PathVariable long performanceId, @RequestBody CommentCreateRequest commentCreateRequest) {
        System.out.println("----before service");
        boolean isSuccess = commentService.createComment(performanceId, commentCreateRequest);
        System.out.println("----after"+isSuccess);
        if (isSuccess) {
            return ApiResponse.onSuccess(HttpStatus.CREATED, "COMMNET201", "댓글 작성 성공", null);
        }
        else {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(@PathVariable long commentId, @RequestBody CommentUpdateRequest commentUpdateRequest){
        boolean isSuccess=true;
        //commentId를 통해서 performanceId를 가져와야함.
        CommentUpdateResponse commentUpdateResponse=CommentUpdateResponse.from(1);
        if(isSuccess){
            return ApiResponse.onSuccess(commentUpdateResponse);
        }
        else {
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

    }
}
