package org.socialculture.platform.member.oauth.naver.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;
import org.socialculture.platform.member.oauth.naver.dto.NaverUserInfoResponseDTO;
import org.socialculture.platform.member.oauth.naver.service.NaverClientService;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 네이버 OAuth 인가코드를 받아온 후 콜백을 처리하고, 사용자 정보를 조회하는 컨트롤러 클래스
 * @author 김연수
 */
@RestController
@Slf4j
@RequestMapping("/api/v1/members/naver")
@RequiredArgsConstructor
public class NaverClientController {
    private final NaverClientService naverClient;
    private final MemberService memberService;


    /**
     * 네이버 API로 인가코드 요청 (테스트를 위한 코드로, 원래는 프론트 코드임)
     * @return 인가코드 요청 URL
     */
    @GetMapping
    public String getNaverLoginURL() {
        String naverLoginURL = naverClient.getLoginURL();
        System.out.println(naverLoginURL);
        return naverLoginURL;
    }

    /**
     * 인가코드로 네이버 사용자 정보 요청
     * @param code 인가코드
     * @param state 상태코드
     */
    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<String>> naverCallBack(@RequestParam String code, @RequestParam String state, HttpSession session) {

        //인가코드로 액세스 토큰 요청
        String naverAccessToken = naverClient.getAccessToken(code, state);

        //액세스 토큰으로 사용자 정보 요청
        SocialMemberCheckDto naverUserInfo = naverClient.getMemberInfo(naverAccessToken);

        if (memberService.isSocialMemberRegistered(naverUserInfo, session)) {
            // 소셜 사용자 로그인 진행
            return ApiResponse.onSuccess("token");
        }else{
            // 회원 가입 진행
            throw new GeneralException(ErrorStatus.SOCIAL_NAME_REQUIRED);
        }
    }

    /**
     * 소셜 사용자가 회원가입시 닉네임 중복체크를 하고, 회원가입 진행
     * @param memberInfoDto
     * @param session
     * @return 회원가입 완료시 프론트에서 로그인페이지로 redirect
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody SocialMemberInfoDto memberInfoDto,
            HttpSession session) {

        memberService.registerSocialUserFromSession(memberInfoDto, session);

        return ApiResponse.onSuccess();


    }


}
