package org.socialculture.platform.member.oauth.kakao.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.member.oauth.kakao.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/member/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    /**
     * 클라이언트에서 카카오 로그인을 했을때 이메일 따옴
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/auth")
    public ResponseEntity<?> callback(@RequestParam("code") String code)
            throws JsonProcessingException {

        String accessToken = kakaoService.getAccessToken(code);
        String email = kakaoService.getUserInfo(accessToken);

        log.info("accessToken = {}", accessToken);
        log.info("email = {}", email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
