package org.socialculture.platform.performance.dto;

import lombok.Getter;
import org.socialculture.platform.performance.entity.PerformanceStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PerformanceWithoutCategory {
    private String memberName;
    private Long performanceId;
    private String title;
    private LocalDateTime dateStartTime;
    private LocalDateTime dateEndTime;
    private String address;
    private String imageUrl;
    int price;
    private PerformanceStatus status;
    private List<CategoryDTO> categories;

    private PerformanceWithoutCategory() {}

    public PerformanceWithoutCategory(String memberName, Long performanceId, String title, LocalDateTime dateStartTime, LocalDateTime dateEndTime, String address, String imageUrl, int price, PerformanceStatus status) {
        this.memberName = memberName;
        this.performanceId = performanceId;
        this.title = title;
        this.dateStartTime = dateStartTime;
        this.dateEndTime = dateEndTime;
        this.address = address;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
    }

    public void updateCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
