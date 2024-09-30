package org.socialculture.platform.comment.controller;

import org.socialculture.platform.comment.dto.CommentReadDto;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {
    @GetMapping("/comments/{performanceId}")
    public ResponseEntity<ApiResponse<List<CommentReadDto>>> getComment(@PathVariable Long performanceId){
        List<CommentReadDto> commentReadDtos= new ArrayList<>();
        return ApiResponse.onSuccess(commentReadDtos);
    }
}
