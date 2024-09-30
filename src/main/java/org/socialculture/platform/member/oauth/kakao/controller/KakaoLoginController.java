package org.socialculture.platform.member.oauth.kakao.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.dto.request.SocialRegisterRequest;
import org.socialculture.platform.member.dto.response.RegisterResponse;
import org.socialculture.platform.member.entity.SocialProvider;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;
import org.socialculture.platform.member.oauth.kakao.service.KakaoClient;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/members/kakao")
public class KakaoLoginController {

    private final KakaoClient kakaoClient;
    private final MemberService memberService;

    public KakaoLoginController(KakaoClient kakaoClient, MemberService memberService) {
        this.kakaoClient = kakaoClient;
        this.memberService = memberService;
    }

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

        try {
            if (memberService.isSocialMemberRegistered(kakaoUserInfo, session)) {
                // 소셜 사용자 로그인 진행
                System.out.println("소셜 사용자 로그인 진행");
                String result = "로그인 진행하면됨";
                return ApiResponse.onSuccess(result);
            } else {  // 소셜 사용자 회원가입 진행
                // 닉네임 정하는 화면으로 리다이렉트
                String result = "닉네임 정하는 화면으로 리다이렉트";
                return ApiResponse.onSuccess(result);
            }
        // 네이버로 가입한 회원이 같은 이메일을 사용하는 카카오로 로그인을 시도하는 경우
        } catch (RuntimeException e) {
            throw new GeneralException(ErrorStatus.SOCIAL_EMAIL_DUPLICATE);
        }
    }


    /**
     * 소셜 사용자가 회원가입시 닉네임 중복체크를 하고, 회원가입 진행
     * @param memberInfoDto
     * @param session
     * @return 회원가입 완료되면 MemberEmtity 반환
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestBody SocialMemberInfoDto memberInfoDto, HttpSession session) {
        try {
            if (!memberService.isNameInUse(memberInfoDto.name())) {
                SocialRegisterRequest socialRegisterRequest = SocialRegisterRequest.create(
                        (String) session.getAttribute("email"),
                        (String) session.getAttribute("providerId"),
                        memberInfoDto.name(),
                        (SocialProvider) session.getAttribute("provider")
                );
                RegisterResponse registerResponse = memberService.registerSocialUser(socialRegisterRequest);
                return ApiResponse.onSuccess(registerResponse);
            }else{
                throw  new GeneralException(ErrorStatus.SOCIAL_NAME_DUPLICATE);
            }
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.SOCIAL_NAME_INVALID);
        }
    }








}
