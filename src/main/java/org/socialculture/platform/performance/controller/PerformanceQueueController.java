package org.socialculture.platform.performance.controller;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.performance.dto.response.PerformanceQueueResponse;
import org.socialculture.platform.performance.service.PerformanceQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performances")
@Slf4j
public class PerformanceQueueController {

    @Autowired
    private PerformanceQueueService queueService;

   //대기열에 사용자 추가
    @PostMapping("/queue")
    public ResponseEntity<ApiResponse<PerformanceQueueResponse>> enterQueue(@AuthenticationPrincipal UserDetails userDetails ,@RequestParam("performanceId") Long performanceId) {


        String  userEmail = userDetails.getUsername();
        Long performance = performanceId;// 공연별로 대기열 구축하는 확장성 고려

        log.info("컨트롤러 userName " +userEmail);
        log.info("컨트롤러 performanceId " +performanceId);
        // Redis에 사용자 대기열 추가
        queueService.addToQueue(userEmail, performanceId);

        Long rank = queueService.getRank(userEmail, performanceId);

        PerformanceQueueResponse performanceQueueResponse = PerformanceQueueResponse.from(userEmail,rank);

        return ApiResponse.onSuccess(performanceQueueResponse);
    }




}
