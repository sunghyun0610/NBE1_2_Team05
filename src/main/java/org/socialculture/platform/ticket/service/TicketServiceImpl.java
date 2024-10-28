package org.socialculture.platform.ticket.service;

import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TicketServiceImpl implements TicketService {

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
     * 티켓을 ID로 조회하여 존재 여부 확인
     */
    private TicketEntity findTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._TICKET_NOT_FOUND));
    }


    //내부에서 사용 - 비관적락 적용시킨 performanceEntity 가져오기(by performanceId)
    private PerformanceEntity findPerformanceByIdWithLock(Long performanceId) {
        PerformanceEntity performance;
        performance = performanceRepository.findByIdWithLock(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
        return performance;
    }

    //내부에서 사용 - 티켓 수량 체크 validation 메소드
    private void validateTicketAvailablity(PerformanceEntity performanceEntity, TicketRequestDto ticketRequest) {
        if (performanceEntity.getRemainingTickets() < ticketRequest.quantity()) {
            log.warn("남은 티켓 수 부족, 공연 ID: " + ticketRequest.performanceId());
            throw new GeneralException(ErrorStatus._NOT_ENOUGH_TICKETS);
        }
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

        MemberEntity memberEntity = findMemberByEmail(email);

        PerformanceEntity performanceEntity = findPerformanceByIdWithLock(ticketRequest.performanceId());

        validateTicketAvailablity(performanceEntity, ticketRequest);

        int finalPrice = calculateFinalPrice(performanceEntity.getPrice(), ticketRequest.quantity(), ticketRequest.couponId());

        TicketEntity ticketEntity = createAndSaveTicket(memberEntity, performanceEntity, ticketRequest.quantity(), finalPrice);

        int remainTicket = calculateRemainTickets(performanceEntity, ticketEntity);

        performanceEntity.updateTicket(remainTicket);

        return TicketResponseDto.fromEntity(ticketEntity);
    }


    /**
     * 티켓 취소
     */
    @Override
    @Transactional
    public void deleteTicket(String email, Long ticketId) {

        TicketEntity ticketEntity = findTicketById(ticketId);

        validateTicketOwnership(ticketEntity, email);

        adjustRemainingTickets(ticketEntity);
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

    private int calculateRemainTickets(PerformanceEntity performanceEntity, TicketEntity ticketEntity) {
        int remainTickets = performanceEntity.getRemainingTickets() - ticketEntity.getQuantity();
        return remainTickets;
    }

    /**
     * 남은 티켓 수량 조정
     */
    private void adjustRemainingTickets(TicketEntity ticketEntity) {
        int ticketNum = ticketEntity.getQuantity();
        PerformanceEntity performanceEntity = findPerformanceByIdWithLock(ticketEntity.getPerformance().getPerformanceId());
        int updateTicket = performanceEntity.getRemainingTickets() + ticketNum;
        performanceEntity.updateTicket(updateTicket);
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

    /**
     * 티켓 소유자인지 확인
     */
    private void validateTicketOwnership(TicketEntity ticketEntity, String email) {
        if (!ticketEntity.getMember().getEmail().equals(email)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
    }
}
