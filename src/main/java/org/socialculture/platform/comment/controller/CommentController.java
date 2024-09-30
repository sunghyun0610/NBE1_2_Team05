package org.socialculture.platform.comment.controller;

import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class CommentController {
    @GetMapping("/comments/{performanceId}")
    public ResponseEntity<ApiResponse<Void>> getComment(@PathVariable Long performanceId){
        return ApiResponse.onSuccess();
    }
}
