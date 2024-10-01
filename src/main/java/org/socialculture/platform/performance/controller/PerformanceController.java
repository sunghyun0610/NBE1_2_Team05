package org.socialculture.platform.performance.controller;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterRequest;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterResponse;
import org.socialculture.platform.performance.dto.request.PerformanceUpdateRequest;
import org.socialculture.platform.performance.dto.response.PerformanceDetailResponse;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.dto.response.PerformanceUpdateResponse;
import org.socialculture.platform.performance.service.PerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    /**
     * @author Icecoff22
     * @param registerPerformanceRequest
     * @return 200
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PerformanceRegisterResponse>> registerPerformance(
            @RequestBody PerformanceRegisterRequest registerPerformanceRequest
    ) {
        return ApiResponse.onSuccess(performanceService.registerPerformance(registerPerformanceRequest));
    }

    /**
     * 공연에 대한 전반적인 정보를 모두 조회.
     * 단, 확정되지 않은 공연은 조회하지 않는다.
     * @author Icecoff22
     * @param page
     * @param size
     * @return 200, 공연응답 리스트
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PerformanceListResponse>>> getPerformanceList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ApiResponse.onSuccess(performanceService.getPerformanceList(page, size));
    }

    /**
     * 공연에 대한 전반적인 정보를 모두 조회.
     * 단, 확정되지 않은 공연은 조회하지 않는다.
     * @author Icecoff22
     * @return 200, 공연응답 리스트
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<PerformanceDetailResponse>> getPerformanceById(@PathVariable("performanceId") Long performanceId) {
        return ApiResponse.onSuccess(performanceService.getPerformanceDetail(performanceId));
    }

    @PatchMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<PerformanceUpdateResponse>> updatePerformance(
            @PathVariable("performanceId") Long performanceId,
            @RequestBody PerformanceUpdateRequest performanceUpdateRequest
    ) {
        return ApiResponse.onSuccess(performanceService.updatePerformance(performanceId, performanceUpdateRequest));
    }
}
