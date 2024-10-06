package org.socialculture.platform.member.auth.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.RefreshTokenEntity;
import org.socialculture.platform.member.auth.JwtTokenProvider;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.member.repository.RefreshTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 리프레시 토큰 유효성 검사
     *      - DB에 저장되어 있는 리프레시 토큰과 비교
     *          - 액세스 토큰으로 email 추출
     *          - email로 memberEntity
     *          - memberId로 DB에 저장되어 있는 리프레시 토큰 추출
     *          - 두 리프레시 토큰이 일치하고, 리프레시 토큰의 유효기간이 유효하면 리프레시 토큰 유효
     *          - 그렇지 않으면 리프레시 토큰 유효 X
     * @param refreshToken
     * @return
     */
    public boolean validateRefreshToken(String refreshToken) {
        String email = jwtTokenProvider.getMemberEmailFromToken(refreshToken);

        MemberEntity member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) return false;

        RefreshTokenEntity storedRefreshToken = refreshTokenRepository.findByMember(member).orElse(null);
        if (storedRefreshToken == null) return false;

        return storedRefreshToken.getRefreshToken().equals(refreshToken);
    }


    /**
     * 리프레시 토큰으로 새로운 토큰 발급
     * @param refreshToken
     * @return
     */
    public String createNewAccessToken(String refreshToken) {

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 인증 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 새로운 액세스 토큰 생성
        return jwtTokenProvider.createAccessToken(authentication);
    }
}
