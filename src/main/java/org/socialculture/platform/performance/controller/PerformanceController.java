package org.socialculture.platform.performance.controller;

import org.socialculture.platform.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping
    public ResponseEntity<?> getPerformanceList(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok(performanceService.getPerformanceList(page, size));
    }
}
