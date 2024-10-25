package org.socialculture.platform.performance.dto.domain;

import lombok.Getter;
import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * query dsl로 모든 공연 정보를 받는 dto
 */
@Getter
public class PerformanceWithCategory {
    private String memberName;
    private Long performanceId;
    private String title;
    private LocalDateTime dateStartTime;
    private LocalDateTime dateEndTime;
    private String address;
    private String imageUrl;
    int price;
    private PerformanceStatus status;
    private Integer remainingTicket;
    private List<CategoryContent> categories;

    private PerformanceWithCategory() {}

    public PerformanceWithCategory(String memberName, Long performanceId, String title, LocalDateTime dateStartTime, LocalDateTime dateEndTime, String address, String imageUrl, int price, PerformanceStatus status, Integer remainingTicket) {
        this.memberName = memberName;
        this.performanceId = performanceId;
        this.title = title;
        this.dateStartTime = dateStartTime;
        this.dateEndTime = dateEndTime;
        this.address = address;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
        this.remainingTicket =  remainingTicket;
    }

    public void updateCategories(List<CategoryContent> categories) {
        this.categories = categories;
    }
}
