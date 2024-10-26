package org.socialculture.platform.ticket.service;

import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialculture.platform.coupon.entity.CouponEntity;
import org.socialculture.platform.coupon.repository.CouponRepository;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.performance.dto.request.PerformanceUpdateRequest;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.socialculture.platform.ticket.dto.request.TicketRequestDto;
import org.socialculture.platform.ticket.dto.response.TicketResponseDto;
import org.socialculture.platform.ticket.entity.TicketEntity;
import org.socialculture.platform.ticket.repository.TicketRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 티켓 서비스 구현체
 *
 * @author ycjung
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final MemberRepository memberRepository;
    private final PerformanceRepository performanceRepository;
    private final CouponRepository couponRepository;
    private final TicketRepository ticketRepository;

    // 내부에서 사용 - 회원 정보 Entity 가져오기(by email)
    private MemberEntity findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));
    }

    // 내부에서 사용 - 공연 정보 Entity 가져오기(by performanceId)
    private PerformanceEntity findPerformanceById(Long performanceId) {
        return performanceRepository.findById(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
    }

    /**
     * 티켓 정보 전체 조회(회원 체크, 페이징, Sort-ASC|DESC)
     */
    @Override
    public List<TicketResponseDto> getAllTicketsByEmailWithPageAndSortOption(String email, int page, int size, String sortOption, boolean isAscending) {
        // Pageable 객체 생성 (정렬은 sortOption과 isAscending에 기반하여 설정)
        Sort sort = isAscending ? Sort.by(sortOption).ascending() : Sort.by(sortOption).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ticketRepository.getAllTicketsByEmailWithPageAndSortOption(email, pageable)
                .stream()
                .map(TicketResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 티켓 상세 조회
     */
    @Override
    public TicketResponseDto getTicketByEmailAndTicketId(String email, Long ticketId) {
        TicketEntity ticketEntity = ticketRepository.getTicketByEmailAndTicketId(email, ticketId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._TICKET_NOT_FOUND));

        return TicketResponseDto.fromEntity(ticketEntity);
    }

    /**
     * 티켓 발권
     */
    @Override
    @Transactional
    public TicketResponseDto registerTicket(String email, TicketRequestDto ticketRequest) {
        log.info("티켓 등록 트랜잭션 시작, 사용자 이메일: " + email);

        MemberEntity memberEntity = findMemberByEmail(email);

        log.info("공연에 대한 락을 획득 시도 중, 공연 ID: " + email);

        PerformanceEntity performanceEntity;
        try {
            performanceEntity = performanceRepository.findByIdWithLock(ticketRequest.performanceId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
        } catch (PessimisticLockException e) {
            log.error("공연 락 획득 실패, 공연 ID: " + email);
            throw e;
        }

       log.info("공연 락 획득 완료, 공연 ID: " + email);

        if (performanceEntity.getRemainingTickets() < ticketRequest.quantity()) {
            log.warn("남은 티켓 수 부족, 공연 ID: " + ticketRequest.performanceId());
            throw new GeneralException(ErrorStatus._NOT_ENOUGH_TICKETS);
        }
        //비관적 락이 걸린 후에 대기 시간 추가 (시각적으로 확인하기 위함)
//        try {
//            Thread.sleep(10000);  // 5초 동안 대기
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException(e);
//        }

        int finalPrice = calculateFinalPrice(performanceEntity.getPrice(), ticketRequest.quantity(), ticketRequest.couponId());

        TicketEntity ticketEntity = createAndSaveTicket(memberEntity, performanceEntity, ticketRequest.quantity(), finalPrice);

        int remainTicket = performanceEntity.getRemainingTickets() - ticketEntity.getQuantity();
        log.info("티켓 등록 후 남은 티켓 수량: " + remainTicket);

        performanceEntity.updateTicket(remainTicket);

        log.info("공연 테이블의 남은 티켓 수 업데이트 완료: " + performanceEntity.getRemainingTickets());

        log.info("트랜잭션 완료, 사용자 이메일: " + email);

        return TicketResponseDto.fromEntity(ticketEntity);
    }


    /**
     * 티켓 취소
     */
    @Override
    @Transactional
    public void deleteTicket(String email, Long ticketId) {
        TicketEntity ticketEntity = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._TICKET_NOT_FOUND));

        // 이메일이 티켓 소유자인지 확인
        if (!ticketEntity.getMember().getEmail().equals(email)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN); // 권한 없음 예외
        }

        int ticketNum=ticketEntity.getQuantity();
        PerformanceEntity performanceEntity;
        try {
          performanceEntity=performanceRepository.findByIdWithLock(ticketEntity.getPerformance().getPerformanceId())
                  .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
        }catch (PessimisticLockException e){
            log.error("공연 락 획득 실패, 공연 ID: " + email);
            throw e;
        }
        int updateTicket = performanceEntity.getRemainingTickets() + ticketNum;
        performanceEntity.updateTicket(updateTicket);

//        performanceRepository.save(performanceEntity); 영속성컨텍스트에서 flush commit날려주므로 save할 필요없을듯

        // 티켓 삭제
        ticketRepository.deleteById(ticketId);

        /**
         * TODO : 티켓 취소 시에 환불 절차 필요.
         */
    }

    private int calculateFinalPrice(int performancePrice, int quantity, Long couponId) {
        int discountPercent = 0;
        int finalPrice = performancePrice * quantity;

        if (Objects.nonNull(couponId)) {
            // 쿠폰 아이디를 통해 쿠폰을 조회하고 내부에서 쿠폰 유효성을 검증한다.
            CouponEntity couponEntity = findAndValidateCoupon(couponId);

            // 할인율 적용
            discountPercent = couponEntity.getPercent();
            finalPrice = calculateDiscountedPrice(performancePrice, quantity, discountPercent);

            // 쿠폰 사용 처리
            couponEntity.setUsed(true); // 쿠폰 사용된 상태로 변경
            couponRepository.save(couponEntity); // 쿠폰 상태 수정 반영
        }

        return finalPrice;
    }

    public int calculateDiscountedPrice(int performancePrice, int quantity, int discountPercent) {
        // 입력 값 유효성 검증
        if (performancePrice < 0 || quantity < 0 || discountPercent < 0) { // 혹시 몰라, 서버 에러 처리
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

        // 총 가격 계산 (공연 가격 * 인원 수)
        int totalPrice = performancePrice * quantity;

        // 할인 금액 계산
        int discountAmount = (totalPrice * discountPercent) / 100;

        // 최종 가격 계산 (총 가격 - 할인 금액)
        int finalPrice = totalPrice - discountAmount;

        return finalPrice;
    }

    private CouponEntity findAndValidateCoupon(Long couponId) {
        CouponEntity couponEntity = couponRepository.findById(couponId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._COUPON_NOT_FOUND));

        if (couponEntity.isUsed()) { // 사용 가능한 쿠폰인 지.
            throw new GeneralException(ErrorStatus._COUPON_ALREADY_USED);
        }

        if (couponEntity.getExpireTime().isBefore(LocalDateTime.now())) { // 쿠폰의 만료시간이 지나지 않았는 지.
            throw new GeneralException(ErrorStatus._COUPON_EXPIRED);
        }

        return couponEntity;
    }

    private TicketEntity createAndSaveTicket(MemberEntity memberEntity, PerformanceEntity performanceEntity, int quantity, int finalPrice) {
        TicketEntity ticketEntity = TicketEntity.builder()
                .member(memberEntity)
                .performance(performanceEntity)
                .dateTime(LocalDateTime.now())
                .quantity(quantity)
                .price(finalPrice)
                .deletedAt(LocalDateTime.now().plusMonths(3)) // 현재 시간으로부터 3개월 후로 설정
                .build();

        return ticketRepository.save(ticketEntity);
    }
}
