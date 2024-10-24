package org.socialculture.platform.performance.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author yechanKim
 */
@Service
@RequiredArgsConstructor
public class PerformanceViewCountServiceImpl implements PerformanceViewCountService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PerformanceService performanceService;

    private static final String RECENT_VIEW_COUNT_KEY = "recent_view_count";
    private static final String PERFORMANCE_VIEW_COUNT_KEY = "performance_view_count";
    private static final int MAX_RECENT_VIEWS = 10;


    // 공연 조회시 해당 performanceId 값을 List에 추가
    @Override
    public void incrementViewCount(Long performanceId) {
        redisTemplate.opsForList().leftPush(RECENT_VIEW_COUNT_KEY, performanceId);
        redisTemplate.opsForZSet().incrementScore(PERFORMANCE_VIEW_COUNT_KEY, performanceId, 1);

        if (redisTemplate.opsForList().size(RECENT_VIEW_COUNT_KEY) > MAX_RECENT_VIEWS) {
            Object oldestPerformanceId = redisTemplate.opsForList().rightPop(RECENT_VIEW_COUNT_KEY);
            redisTemplate.opsForZSet().incrementScore(PERFORMANCE_VIEW_COUNT_KEY, oldestPerformanceId, -1);
        }
    }


    // 최근 조회수중 가장 높은 10개의 공연 조회
    @Override
    public PerformanceListResponse getTopPerformanceIds() {
        Set<Object> topPerformances = redisTemplate.opsForZSet()
                .reverseRange(PERFORMANCE_VIEW_COUNT_KEY, 0, 9);

        List<Long> performanceIds = new ArrayList<>();
        if (topPerformances != null) {
            for (Object topPerformance : topPerformances) {
                performanceIds.add(Long.valueOf(topPerformance.toString()));
            }
//            performanceService.getTopPerformancesIds(performanceIds);
        }
        return PerformanceListResponse.from(0, Collections.emptyList());

    }








}
