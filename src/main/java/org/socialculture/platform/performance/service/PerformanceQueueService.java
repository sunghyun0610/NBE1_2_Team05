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

    private static final String QUEUE_KEY = "ticket_queue";
    private static final int MAX_QUEUE_SIZE = 200; // 큐 최대 크기 설정

    // 대기열에 사용자 추가
    public boolean addToQueue(String userEmail) {
        Long currentQueueSize = redisTemplate.opsForZSet().zCard(QUEUE_KEY);
        if (currentQueueSize != null && currentQueueSize >= MAX_QUEUE_SIZE) {
            throw new GeneralException(ErrorStatus._NOT_ENOUGH_QUEUE);
        }

        long timestamp = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(QUEUE_KEY, userEmail, timestamp);

        log.info("사용자 {}가 대기열에 추가되었습니다. 점수(순위): {}", userEmail, timestamp);
        return true;
    }


    // 대기열에서 사용자 순위 확인
    public Long getRank(String userEmail) {
        Long rank = redisTemplate.opsForZSet().rank(QUEUE_KEY, userEmail);
        return rank != null ? rank + 1 : -1; // Redis의 rank는 0부터 시작하므로 +1
//        만약 userEmail이 대기열에 없다면 rank는 null을 반환하게 되고, 이 경우 -1을 반환하여 사용자 미존재를 나타냅니다.
    }

    public List<String> processNextBatch(int batchSize) {
        try {
            Set<String> batchUsers = redisTemplate.opsForZSet().range(QUEUE_KEY, 0, batchSize - 1);
            if (batchUsers == null || batchUsers.isEmpty()) {
                log.info("대기열에 사용자가 없습니다.");
                return Collections.emptyList();
            }

            List<String> userEmails = batchUsers.stream().map(String::valueOf).collect(Collectors.toList());
            batchUsers.forEach(user -> redisTemplate.opsForZSet().remove(QUEUE_KEY, user));

            return userEmails;
        } catch (Exception e) {
            log.error("대기열에서 다음 배치 처리를 수행하는 중 오류가 발생했습니다.", e);
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }


    // 일정 간격으로 대기열 배치 처리
    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void scheduledProcessBatch() {

        Long queueSize = redisTemplate.opsForZSet().zCard(QUEUE_KEY);
        if(queueSize!=null && queueSize>0) {
            List<String> processeduserEmails = processNextBatch(10);
            if (!processeduserEmails.isEmpty()) {
                log.info("Processed users: " + processeduserEmails);
                // 추가 처리가 필요한 경우(알림, 상태 업데이트 등)를 여기서 수행 가능
            }
        }
    }


}
