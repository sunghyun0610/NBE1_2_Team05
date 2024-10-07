package org.socialculture.platform.performance.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.performance.dto.request.PerformanceRegisterRequest;
import org.socialculture.platform.performance.dto.response.PerformanceRegisterResponse;
import org.socialculture.platform.performance.dto.request.PerformanceUpdateRequest;
import org.socialculture.platform.performance.dto.response.PerformanceDetailResponse;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.socialculture.platform.performance.dto.response.PerformanceUpdateResponse;
import org.socialculture.platform.performance.service.PerformanceService;
import org.springframework.http.HttpStatus;
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
     * @return 200, 등록 완료 메세지
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PerformanceRegisterResponse>> registerPerformance(
            @RequestBody PerformanceRegisterRequest registerPerformanceRequest
    ) {
        PerformanceRegisterResponse performanceRegisterResponse = performanceService.registerPerformance(registerPerformanceRequest);

        return ApiResponse.onSuccess(
                HttpStatus.CREATED,
                "PERFORMANCE201",
                "공연이 등록되었습니다.",
                performanceRegisterResponse
        );
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
    public ResponseEntity<ApiResponse<List<PerformanceListResponse>>> getPerformanceList(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
    ) {
        return ApiResponse.onSuccess(performanceService.getPerformanceList(page, size));
    }

    /**
     * 특정 공연에 대한 전반적인 정보를 모두 조회.
     * @author Icecoff22
     * @param performanceId
     * @return 200, 공연응답 리스트
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<PerformanceDetailResponse>> getPerformanceById(@PathVariable("performanceId") Long performanceId) {
        return ApiResponse.onSuccess(performanceService.getPerformanceDetail(performanceId));
    }

    /**
     * 공연에 대한 정보를 수정.
     * 자신의 공연만 수정할 수 있다.
     * @author Icecoff22
     * @param performanceId
     * @return 200, 공연수정 정보
     */
    @PatchMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<PerformanceUpdateResponse>> updatePerformance(
            @PathVariable("performanceId") Long performanceId,
            @RequestBody PerformanceUpdateRequest performanceUpdateRequest
    ) {
        return ApiResponse.onSuccess(performanceService.updatePerformance(performanceId, performanceUpdateRequest));
    }

    /**
     * 공연에 대한 삭제.
     * 실 삭제가 아닌 deleteAt만 업데이트 시킨다.
     * 자신의 공연만 삭제할 수 있다.
     * @author Icecoff22
     * @param performanceId
     * @return 200
     */
    @DeleteMapping("/{performanceId}")
    public ResponseEntity<ApiResponse<Void>> deletePerformance(@PathVariable("performanceId") Long performanceId) {
        performanceService.deletePerformance(performanceId);
        return ApiResponse.onSuccess();
    }

    @GetMapping("/admin/my")
    public ResponseEntity<ApiResponse<List<PerformanceListResponse>>> getPerformanceListAdmin(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ApiResponse.onSuccess(performanceService.getMyPerformanceList("email", page, size));
    }
}
