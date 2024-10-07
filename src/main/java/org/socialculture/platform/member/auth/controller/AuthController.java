package org.socialculture.platform.member.auth.controller;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.ApiResponse;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.global.apiResponse.success.SuccessStatus;
import org.socialculture.platform.member.auth.service.AuthService;
import org.socialculture.platform.member.dto.LoginDTO;
import org.socialculture.platform.member.auth.JwtTokenProvider;
import org.socialculture.platform.member.auth.dto.TokenRequestDTO;
import org.socialculture.platform.member.auth.dto.TokenResponseDTO;
import org.socialculture.platform.member.auth.JwtFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;

    /**
     * 로컬 로그인
     * @param loginDTO
     * @return accessToken, refreshToken, message
     */
    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponseDTO> authorize(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        String email = jwtTokenProvider.getMemberEmailFromToken(accessToken);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer" + accessToken);

        String message = "액세스 토큰과 리프레시 토큰이 정상적으로 발급되었습니다.";
        return new ResponseEntity<>(new TokenResponseDTO(accessToken, refreshToken, message), httpHeaders, HttpStatus.OK);
    }

    //토큰 유효성 검사 테스트용
    @PostMapping("/check")
    public void checkToken() {
        System.out.println("토큰 체크");
    }


    /**
     * 리프레시 토큰 유효성 검사
     * @param tokenRequestDTO
     * @return accessToken, refreshToken
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<TokenResponseDTO>> validateRefreshToken(@RequestBody TokenRequestDTO tokenRequestDTO) {
        String refreshToken = tokenRequestDTO.getRefreshToken();

        if (!authService.validateRefreshToken(refreshToken)) { //리프레시 토큰 유효하지 않을 때
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = authService.createNewAccessToken(refreshToken);

        TokenResponseDTO result = new TokenResponseDTO(newAccessToken, refreshToken);
        return ApiResponse.onSuccess(HttpStatus.CREATED, "COMMON200", SuccessStatus._CREATED.getMessage(), result);
    }
}
