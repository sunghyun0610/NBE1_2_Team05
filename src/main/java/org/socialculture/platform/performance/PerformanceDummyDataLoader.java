package org.socialculture.platform.performance;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.entity.PerformanceStatus;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Profile("local")
@RequiredArgsConstructor
public class PerformanceDummyDataLoader implements CommandLineRunner {

    private final PerformanceRepository performanceRepository;
    private final MemberRepository memberRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public void run(String... args) {
        List<MemberEntity> members = memberRepository.findAll();
        if (members.isEmpty()) {
            System.out.println("⚠️ Member 데이터가 없습니다. 먼저 회원 데이터를 넣어주세요.");
            return;
        }

        final int TOTAL_COUNT = 1_000_0000;
        final int BATCH_SIZE = 10000;

        Random random = new Random();
        List<PerformanceEntity> batch = new ArrayList<>(BATCH_SIZE);

        for (int i = 1; i <= TOTAL_COUNT; i++) {
            MemberEntity member = members.get(i % members.size());

            PerformanceEntity performance = PerformanceEntity.builder()
                    .member(member)
                    .title("공연 " + i)
                    .description("설명 " + i)
                    .dateStartTime(LocalDateTime.now().plusDays(i % 30))
                    .dateEndTime(LocalDateTime.now().plusDays(i % 30).plusHours(2))
                    .startDate(LocalDateTime.now().minusDays(10))
                    .maxAudience(100 + (i % 5) * 50)
                    .remainingTickets(random.nextInt(100))
                    .price((i % 3 + 1) * 5000)
                    .address("서울시 구" + (i % 25))
                    .location("서울시 구" + (i % 25) + " 공연장 " + i)
                    .coordinate(geometryFactory.createPoint(new Coordinate(
                            126.8 + random.nextDouble() * 0.4,
                            37.4 + random.nextDouble() * 0.4)))
                    .imageUrl("https://example.com/image" + i + ".jpg")
                    .performanceStatus(i % 2 == 0 ? PerformanceStatus.CANCELED : PerformanceStatus.CONFIRMED)
                    .build();

            batch.add(performance);

            if (i % BATCH_SIZE == 0) {
                performanceRepository.saveAll(batch);
                batch.clear();
                System.out.println("✅ Inserted: " + i);
            }
        }

        // 혹시 남은 데이터가 있다면 추가로 저장
        if (!batch.isEmpty()) {
            performanceRepository.saveAll(batch);
            System.out.println("✅ Inserted 나머지: " + batch.size() + "건");
        }

        System.out.println("🎉 전체 100만 건 더미 데이터 삽입 완료!");
    }
}
