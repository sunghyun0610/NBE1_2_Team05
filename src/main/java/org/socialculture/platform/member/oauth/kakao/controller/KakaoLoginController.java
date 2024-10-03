package org.socialculture.platform.member.oauth.kakao.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;
import org.socialculture.platform.member.oauth.kakao.service.KakaoClient;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/api/v1/members/kakao")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoClient kakaoClient;
    private final MemberService memberService;


    /**
     * 클라이언트에서 카카오 로그인을 했을때 사용자 정보를 얻어서 확인 후,
     * 소셜 로그인 혹은 회원가입 진행
     * @param code
     * @return 가입된 사용자는 로그인, 가입 되어있지 않으면 회원가입
     * @throws JsonProcessingException
     */
    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<String>> callback(@RequestParam("code") String code,
                                                        HttpSession session)
            throws JsonProcessingException {
        SocialMemberCheckDto kakaoUserInfo = kakaoClient
                .getUserInfo(kakaoClient.getAccessToken(code));

        if (memberService.isSocialMemberRegistered(kakaoUserInfo, session)) {
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
