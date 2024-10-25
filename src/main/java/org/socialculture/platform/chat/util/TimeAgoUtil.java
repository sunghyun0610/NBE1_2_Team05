package org.socialculture.platform.chat.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeAgoUtil {
    public static String getElapsedTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long weeks = days / 7;
        long months = ChronoUnit.MONTHS.between(createdAt, now);
        long years = ChronoUnit.YEARS.between(createdAt, now);

        if (years > 0) {
            return years + "년 전";
        } else if (months > 0) {
            return months + "달 전";
        } else if (weeks > 0) {
            return weeks + "주 전";
        } else if (days > 0) {
            return days + "일 전";
        } else if (hours > 0) {
            return hours + "시간 전";
        } else if (minutes > 0) {
            return minutes + "분 전";
        } else {
            return seconds + "초 전";
        }
    }

    public static void main(String[] args) {
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 23, 14, 30);
        System.out.println(getElapsedTime(createdAt));
    }
}
