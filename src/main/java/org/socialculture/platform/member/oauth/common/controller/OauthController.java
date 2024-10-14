package org.socialculture.platform.member.oauth.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.auth.JwtFilter;
import org.socialculture.platform.member.auth.dto.TokenResponseDTO;
import org.socialculture.platform.member.auth.service.AuthService;
import org.socialculture.platform.member.dto.request.SocialRegisterRequest;
import org.socialculture.platform.member.entity.SocialProvider;
import org.socialculture.platform.member.oauth.common.dto.SocialLoginRequest;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberInfoDto;
import org.socialculture.platform.member.oauth.common.service.SocialClient;
import org.socialculture.platform.member.oauth.kakao.service.KakaoClient;
import org.socialculture.platform.member.oauth.naver.service.NaverClient;
import org.socialculture.platform.member.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 네이버,카카오 소셜 로그인 컨트롤러 통합
 * @author 김예찬
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/oauth")
public class OauthController {

    private final MemberService memberService;
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final AuthService authService;


    /**
     * provider를 구분하여 각각의 client에서
     * 회원정보 얻고, 가입되어있는지 여부 체크 후
     * 로그인 or 회원가입 실행
     * @param provider
     * @param socialLoginRequest
     * @param session
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/{provider}")
    public ResponseEntity<?> socialCallback(
            @PathVariable("provider") SocialProvider provider,
            @RequestBody SocialLoginRequest socialLoginRequest,
            HttpSession session) throws JsonProcessingException {

        SocialClient clientByProvider = getClientByProvider(provider);

        String accessToken = (provider == SocialProvider.NAVER) ?
                clientByProvider.getAccessToken(socialLoginRequest.code(),
                        socialLoginRequest.state())
                : clientByProvider.getAccessToken(socialLoginRequest.code());
        SocialMemberCheckDto memberInfo = clientByProvider.getMemberInfo(accessToken);

        if (memberService.isSocialMemberRegistered(memberInfo)) {
            // 가입되어있으면 토큰 발급
            TokenResponseDTO tokenResponseDTO = authService
                    .createTokenResponseForSocialMember(memberInfo.email());

            return createTokenResponse(tokenResponseDTO);
        }
        // 소셜 사용자 회원가입시 필요한 기본정보 세션에 저장
        session.setAttribute("providerId", memberInfo.providerId());
        session.setAttribute("provider", memberInfo.provider());
        session.setAttribute("email", memberInfo.email());

        session.setMaxInactiveInterval(600); // 임시 회원정보 세션 10분간 유지

        // 추후 클라이언트 서버의 주소가 변경될 경우 이부분 변경 필요
        return ResponseEntity.ok("redirect nicknamePage");
//        return ResponseEntity.status(HttpStatus.OK).body("redirect nicknamePage");
    }



    /**
     * 소셜 사용자 임시정보 세션과 사용자에게 받은 닉네임을 확인 후
     * 회원가입 진행
     * @param memberInfoDto
     * @param session
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SocialMemberInfoDto memberInfoDto,
                                                      HttpSession session) {

        memberService.validateNameAndCheckDuplicate(memberInfoDto.name());
        String name = memberInfoDto.name();
        String email = (String) session.getAttribute("email");
        String providerId = (String) session.getAttribute("providerId");
        SocialProvider provider = (SocialProvider) session.getAttribute("provider");

        if (email == null || provider == null || providerId == null) {
            throw new GeneralException(ErrorStatus.SOCIAL_INFO_INVALID);
        }
        SocialRegisterRequest socialRegisterRequest = SocialRegisterRequest.create(
                email,
                providerId,
                name,
                provider
        );

        memberService.registerSocialMember(socialRegisterRequest, session);
        TokenResponseDTO tokenResponseDTO = authService.createTokenResponseForSocialMember(email);
        return createTokenResponse(tokenResponseDTO);
    }



    // provider에 맞는 Client 가져오기
    private SocialClient getClientByProvider(SocialProvider provider) {
        if (provider == SocialProvider.NAVER) {
            return naverClient;
        } else if (provider == SocialProvider.KAKAO) {
            return kakaoClient;
        } else{
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    // 소셜 사용자 로그인 후 토큰 발급
    private ResponseEntity<TokenResponseDTO> createTokenResponse(TokenResponseDTO tokenResponseDTO) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer "
                + tokenResponseDTO.getAccessToken());

        return new ResponseEntity<>(tokenResponseDTO, httpHeaders, HttpStatus.OK);
    }




}
