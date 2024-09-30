package org.socialculture.platform.performance.entity;

import lombok.Getter;

@Getter
public enum PerformanceStatus {
    NOT_CONFIRMED,
    CONFIRMED,
    CANCELED;

    private String status;


}
