package org.socialculture.platform.performance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.performance.dto.response.PerformanceListResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author yechanKim
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PerformanceViewCountServiceImpl implements PerformanceViewCountService {

    @Qualifier("longRedisTemplate")
    private final RedisTemplate<String, Long> longRedisTemplate;
    @Qualifier("performanceRedisTemplate")
    private final RedisTemplate<String, PerformanceListResponse> performanceListResponseRedisTemplate;
    private final PerformanceService performanceService;

    private static final String RECENT_VIEW_COUNT_KEY = "recent_view_count";
    private static final String PERFORMANCE_VIEW_COUNT_KEY = "performance_view_count";
    private static final String PERFORMANCE_VIEW_CACHE_KEY = "popular_performances_cache";
    private static final int MAX_RECENT_VIEWS = 200;
    private static final int SCHEDULER_TIME  = 300000;


    /**
     * 사용자가 특정 공연 조회시 redis에 조회수 증감
     * @param performanceId
     */
    @Override
    public void incrementViewCount(Long performanceId) {
        longRedisTemplate.opsForList().leftPush(RECENT_VIEW_COUNT_KEY, performanceId);
        longRedisTemplate.opsForZSet().incrementScore(PERFORMANCE_VIEW_COUNT_KEY, performanceId, 1);

        Long listSize = longRedisTemplate.opsForList().size(RECENT_VIEW_COUNT_KEY);
        if (listSize != null && listSize > MAX_RECENT_VIEWS) {
            Long oldestPerformanceId = (Long) longRedisTemplate.opsForList().rightPop(RECENT_VIEW_COUNT_KEY);
            if (oldestPerformanceId != null) {
                longRedisTemplate.opsForZSet().incrementScore(PERFORMANCE_VIEW_COUNT_KEY,
                        oldestPerformanceId, -1);
            }
        }
    }

    /**
     * 5분마다 실시간 인기공연 갱신
     * 메인DB에서 공연정보 받아와서 redis에 캐싱
     */
    @Scheduled(fixedRate = SCHEDULER_TIME)
    public void cacheTopPerformance() {
        log.info("실시간 인기공연 스케줄러 동작");

        List<Long> performanceIds = getPopularPerformanceIds();
        if(!performanceIds.isEmpty()){
            PerformanceListResponse popularPerformances = performanceService.getPopularPerformances(performanceIds);

            performanceListResponseRedisTemplate.opsForValue().set(PERFORMANCE_VIEW_CACHE_KEY, popularPerformances);
        }
    }

    /**
     * redis에 캐시에서 실시간 인기공연 조회
     * 캐시에 없다면 mysql에서 직접 조회후 캐싱
     * @return
     */
    @Override
    public PerformanceListResponse getPopularPerformances() {
        PerformanceListResponse performances = (PerformanceListResponse)
                performanceListResponseRedisTemplate.opsForValue().get(PERFORMANCE_VIEW_CACHE_KEY);

        if (performances != null) {
            return performances;
        }

        List<Long> performanceIds = getPopularPerformanceIds();

        if(!performanceIds.isEmpty()){
            PerformanceListResponse popularPerformances = performanceService.getPopularPerformances(performanceIds);
            performanceListResponseRedisTemplate.opsForValue().set(PERFORMANCE_VIEW_CACHE_KEY, popularPerformances);
            return popularPerformances;
        }
        return new PerformanceListResponse(0, Collections.emptyList());
    }

    // 상위 인기공연 10개의 performanceId 반환
    private List<Long> getPopularPerformanceIds() {
        Set<ZSetOperations.TypedTuple<Long>> topPerformanceIds = longRedisTemplate.opsForZSet()
                .reverseRangeWithScores(PERFORMANCE_VIEW_COUNT_KEY, 0, 9);

        if (topPerformanceIds != null && !topPerformanceIds.isEmpty()) {
            return topPerformanceIds.stream().map(ZSetOperations.TypedTuple::getValue)
                    .toList();
        }
        log.info("조회된 공연이 없습니다.");
        return Collections.emptyList();
    }
}
