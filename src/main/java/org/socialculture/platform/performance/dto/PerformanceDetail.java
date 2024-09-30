package org.socialculture.platform.performance.dto;

import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;

public class PerformanceDetailDTO {
    private Long memberId;
    private Long performanceId;
    private String title;
    private LocalDateTime dateStartTime;
    private LocalDateTime dateEndTime;
    private String description;
    private int maxAudience;
    private String address;
    private String imageUrl;
    private int price;
    private int remainingTickets;
    private LocalDateTime startDate;
    private PerformanceStatus performanceStatus;
}
