package org.socialculture.platform.coupon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.service.CouponService;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 쿠폰 데이터 컨트롤러
 * 
 * @author ycjung
 */
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponResponseDto>>> getAllCouponsByMemberEmail(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received request to get all coupons by member email");

        return ApiResponse.onSuccess(couponService.getAllCouponsByMemberEmail(userDetails.getUsername()));
    }
}
