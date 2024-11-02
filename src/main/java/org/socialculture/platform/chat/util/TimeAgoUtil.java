package org.socialculture.platform.chat.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeAgoUtil {
    private static final DateTimeFormatter todayFormatter = DateTimeFormatter.ofPattern("a h:mm"); // 오전/오후 h:mm
    private static final DateTimeFormatter monthDayFormatter = DateTimeFormatter.ofPattern("M월 d일");
    private static final DateTimeFormatter yearMonthDayFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");

    public static String getElapsedTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long days = duration.toDays();
        long years = ChronoUnit.YEARS.between(createdAt, now);

        if (days == 0) {
            // 오늘 날짜인 경우 오전/오후 h:mm 형식
            return createdAt.format(todayFormatter);
        } else if (days == 1) {
            // 1일 전인 경우 '어제'로 표시
            return "어제";
        } else if (years > 0) {
            // 1년 전 이상인 경우 yyyy년 M월 d일 형식
            return createdAt.format(yearMonthDayFormatter);
        } else {
            // 2일 전부터 1년 미만인 경우 M월 d일 형식
            return createdAt.format(monthDayFormatter);
        }
    }

    // 오전/오후 시간 표시
    public static String formatToAmPm(LocalDateTime dateTime) {
        return dateTime.format(todayFormatter);
    }


    public static void main(String[] args) {
        // Case 1: Same Day (Today)
        LocalDateTime createdAtToday = LocalDateTime.now().minusHours(2); // 2시간 전
        System.out.println("Case 1 - Same Day: " + getElapsedTime(createdAtToday)); // Expected: 현재 시간에서 2시간 전의 시간 (예: 오후 1:30)

        // Case 2: Yesterday
        LocalDateTime createdAtYesterday = LocalDateTime.now().minusDays(1); // 어제
        System.out.println("Case 2 - Yesterday: " + getElapsedTime(createdAtYesterday)); // Expected: 어제

        // Case 3: Within the Past Year (Less Than a Year Ago)
        LocalDateTime createdAtPastYear = LocalDateTime.now().minusMonths(3); // 3개월 전
        System.out.println("Case 3 - Within the Past Year: " + getElapsedTime(createdAtPastYear)); // Expected: M월 d일 형식 (예: 7월 10일)

        // Case 4: More Than a Year Ago
        LocalDateTime createdAtMoreThanYear = LocalDateTime.now().minusYears(2); // 2년 전
        System.out.println("Case 4 - More Than a Year Ago: " + getElapsedTime(createdAtMoreThanYear)); // Expected: yyyy년 M월 d일 형식 (예: 2022년 7월 10일)

        // Case 5: Edge of Midnight (Yesterday to Today Transition)
        LocalDateTime createdAtEdgeOfMidnight = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59); // 어제 오후 11:59
        System.out.println("Case 5 - Edge of Midnight: " + getElapsedTime(createdAtEdgeOfMidnight)); // Expected: 어제
    }

}
