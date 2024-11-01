package org.socialculture.platform.performance.service;

import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PerformanceQueueService {

    @Autowired
    @Qualifier("queueRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    private static final int MAX_QUEUE_SIZE = 200; // 큐 최대 크기 설정
    private static final String QUEUE_TRACKER_KEY = "active_performance_queues";

    private String getQueueKey(Long performanceId){
        return "ticket_queue_" + performanceId;
    }

    // 대기열에 사용자 추가
    public boolean addToQueue(String userEmail, Long performanceId) {

        String queueKey = getQueueKey(performanceId);

        Long currentQueueSize = redisTemplate.opsForZSet().zCard(queueKey);
        if (currentQueueSize != null && currentQueueSize >= MAX_QUEUE_SIZE) {
            throw new GeneralException(ErrorStatus._NOT_ENOUGH_QUEUE);
        }

        long timestamp = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(queueKey, userEmail, timestamp);
        redisTemplate.opsForSet().add(QUEUE_TRACKER_KEY, String.valueOf(performanceId));

        log.info("사용자 {}가 공연 {} 대기열에 추가되었습니다. 점수(순위): {}", userEmail, performanceId, timestamp);
        return true;
    }


    // 대기열에서 사용자 순위 확인
    public Long getRank(String userEmail, Long performanceId) {
        String queueKey=getQueueKey(performanceId);

        Long rank = redisTemplate.opsForZSet().rank(queueKey, userEmail);
        return rank != null ? rank + 1 : -1; // Redis의 rank는 0부터 시작하므로 +1
//        만약 userEmail이 대기열에 없다면 rank는 null을 반환하게 되고, 이 경우 -1을 반환하여 사용자 미존재를 나타냅니다.
    }

    // 공연별로 대기열에서 다음 배치 처리
    public List<String> processNextBatch(int batchSize, Long performanceId) {
        String queueKey = getQueueKey(performanceId);
        Set<String> batchUsers = redisTemplate.opsForZSet().range(queueKey, 0, batchSize - 1);
        if (batchUsers == null || batchUsers.isEmpty()) {
            redisTemplate.opsForSet().remove(QUEUE_TRACKER_KEY, String.valueOf(performanceId)); // 대기열 비면 공연 ID 삭제
            log.info("공연 {}의 대기열에 사용자가 없습니다.", performanceId);
            return Collections.emptyList();
        }

        List<String> userEmails = batchUsers.stream().map(String::valueOf).collect(Collectors.toList());
        batchUsers.forEach(user -> redisTemplate.opsForZSet().remove(queueKey, user));

        return userEmails;
    }


    // 일정 간격으로 대기열 배치 처리
    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void scheduledProcessBatch() {
        Set<String> activePerformanceIds = redisTemplate.opsForSet().members(QUEUE_TRACKER_KEY);
        if(activePerformanceIds == null || activePerformanceIds.isEmpty()){
            return;
        }

        for (String performanceIdStr : activePerformanceIds) {
            Long performanceId = Long.parseLong(performanceIdStr);
            List<String> processedUserEmails = processNextBatch(10, performanceId);
            if (!processedUserEmails.isEmpty()) {
                log.info("공연 {}의 처리된 사용자들: {}", performanceId, processedUserEmails);
                // 추가 처리가 필요한 경우(알림, 상태 업데이트 등)를 여기서 수행 가능
            }
        }
    }


}
