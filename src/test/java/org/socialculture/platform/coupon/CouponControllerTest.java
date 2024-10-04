package org.socialculture.platform.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.socialculture.platform.coupon.controller.CouponController;
import org.socialculture.platform.coupon.dto.response.CouponResponseDto;
import org.socialculture.platform.coupon.service.CouponService;
import org.socialculture.platform.performance.controller.PerformanceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
@ExtendWith(SpringExtension.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Test
    @DisplayName("티켓 전체 조회 테스트")
    @WithMockUser
    void getAllCoupons() throws Exception {
        // given
        List<CouponResponseDto> couponList = List.of(
                CouponResponseDto.create(
                        1L,
                        "Discount10",
                        10,
                        false,
                        LocalDateTime.of(2024, 12, 31, 23, 59),
                        LocalDateTime.of(2024, 1, 1, 10, 0)
                ),
                CouponResponseDto.create(
                        2L,
                        "Discount20",
                        20,
                        true,
                        LocalDateTime.of(2025, 6, 30, 23, 59),
                        LocalDateTime.of(2024, 6, 1, 12, 0)
                )
        );

        given(couponService.getAllCouponsByMemberEmail())
                .willReturn(couponList);

        ResultActions result = this.mockMvc.perform(
                get("/api/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
    }
}
