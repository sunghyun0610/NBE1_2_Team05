package org.socialculture.platform.member.auth.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.auth.dto.TokenResponseDTO;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.RefreshTokenEntity;
import org.socialculture.platform.member.auth.JwtTokenProvider;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.member.repository.RefreshTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 리프레시 토큰 유효성 검사
     * - DB에 저장되어 있는 리프레시 토큰과 비교
     * - 액세스 토큰으로 email 추출
     * - email로 memberEntity
     * - memberId로 DB에 저장되어 있는 리프레시 토큰 추출
     * - 두 리프레시 토큰이 일치하고, 리프레시 토큰의 유효기간이 유효하면 리프레시 토큰 유효
     * - 그렇지 않으면 리프레시 토큰 유효 X
     *
     * @param refreshToken
     * @return
     */
    public boolean validateRefreshToken(String refreshToken) {
        String email = jwtTokenProvider.getMemberEmailFromToken(refreshToken);

        MemberEntity member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) return false;

        RefreshTokenEntity storedRefreshToken = refreshTokenRepository.findByMember(member).orElse(null);
        if (storedRefreshToken == null) return false;

        //현재 시간이랑 storedExpiryDate랑 비교
        LocalDateTime storedExpiryDate = storedRefreshToken.getExpiryDate();
        LocalDateTime now = LocalDateTime.now(); // 현재 시간 가져오기
        if (now.isAfter(storedExpiryDate)) return false;

        return storedRefreshToken.getRefreshToken().equals(refreshToken);
    }


    /**
     * 리프레시 토큰으로 새로운 토큰 발급
     *
     * @param refreshToken
     * @return
     */
    public String createNewAccessToken(String refreshToken) {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 새로운 액세스 토큰 생성
        return jwtTokenProvider.createAccessToken(authentication);
    }

    /**
     * 리프레시 토큰 저장
     * @param refreshToken
     */
    @Override
    public void insertRefreshToken(String refreshToken) {
        String email = jwtTokenProvider.getMemberEmailFromToken(refreshToken);
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        //member에 해당하는 리프레시 토큰 삭제
        refreshTokenRepository.deleteAllByMember(member);

        LocalDateTime expiryDate = jwtTokenProvider.getExpiryDate(refreshToken);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .member(member)
                .expiryDate(expiryDate)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }

    /**
     * 소셜 사용자의 토큰발행을 위해 사용
     * @param email
     * @return
     */
    @Override
    public TokenResponseDTO createTokenResponseForSocialMember(String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        insertRefreshToken(refreshToken);

        String message = "액세스 토큰과 리프레시 토큰이 정상적으로 발급되었습니다.";
        return new TokenResponseDTO(
                accessToken, refreshToken, message, memberEntity.getName(),memberEntity.isFirstLogin());
    }



}
