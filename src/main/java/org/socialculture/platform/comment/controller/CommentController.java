package org.socialculture.platform.comment.controller;

import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.service.CommentServiceImpl;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {

    private CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService){
        this.commentService=commentService;
    }


    @GetMapping("/comments/{performanceId}")
    public ResponseEntity<ApiResponse<List<CommentReadDto>>> getComment(@PathVariable long performanceId){
        List<CommentReadDto> commentReadDtos= commentService.getAllComment(performanceId);
        return ApiResponse.onSuccess(commentReadDtos);
    }
}
