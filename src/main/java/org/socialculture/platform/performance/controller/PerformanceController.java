package org.socialculture.platform.performance.controller;

import org.socialculture.platform.performance.service.PerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
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
    public ResponseEntity<?> getPerformanceList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok(performanceService.getPerformanceList(page, size));
    }

    /**
     * 공연에 대한 전반적인 정보를 모두 조회.
     * 단, 확정되지 않은 공연은 조회하지 않는다.
     * @author Icecoff22
     * @return 200, 공연응답 리스트
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<?> getPerformanceById(@PathVariable("performanceId") Long performanceId) {
        return ResponseEntity.ok(performanceService.getPerformanceDetail(performanceId));
    }
}
