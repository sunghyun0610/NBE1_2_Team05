package org.socialculture.platform.member.oauth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.member.oauth.jwt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT 토큰 관리 클래스
 * @author yeonsu
 */
@Component
@Slf4j
public class JwtTokenProvider {
    private final String secretKey; // JWT 비밀키
    private final Key key; // JWT 서명에 사용할 키
    private final long accessTokenValidity; // 액세스 토큰 유효 시간
    private final long refreshTokenValidity; // 리프레시 토큰 유효 시간

    private static final String AUTHORITIES_KEY = "auth"; // 권한 키

    private final CustomUserDetailsService customUserDetailsService;

    // 생성자에서 초기화
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.token.expiration}") long accessTokenValidity,
            @Value("${jwt.refresh.token.expiration}") long refreshTokenValidity,
            CustomUserDetailsService customUserDetailsService) {
        this.secretKey = secretKey;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.customUserDetailsService = customUserDetailsService;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT TokenProvider 초기화 완료");
    }

    //액세스 토큰 생성
    public String createAccessToken(Authentication authentication) {
        // 권한 값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // email을 직접 가져와서 claim에 추가
        String email = authentication.getName(); // 사용자 정보에서 email 가져오기

        return Jwts.builder()
                .setSubject(authentication.getName()) // 페이로드 주제 정보
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보 저장
                .claim("email", email) // email 정보 추가
                .signWith(key, SignatureAlgorithm.HS256) // 서명 설정
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity)) // 만료 시간 설정
                .compact();
    }


    // 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사
    public void validateToken(String token) throws JwtException {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true; // 유효한 토큰
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("잘못된 JWT 서명입니다.");
//        } catch (ExpiredJwtException e) {
//            log.info("만료된 JWT 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.info("지원되지 않는 JWT 토큰입니다.");
//        } catch (IllegalArgumentException e) {
//            log.info("JWT 토큰이 잘못되었습니다.");
//        }
//        return false;
    }

    // 토큰으로 사용자 이메일 가져오기
    public String getMemberEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // 이메일은 subject로 설정
        } catch (JwtException | IllegalArgumentException e) {
            log.info("유효하지 않은 JWT 토큰입니다.");
            return null;
        }
    }

    //리프레시 토큰으로 Authentication 객체 얻기
    public Authentication getAuthentication(String refreshToken) {
        if (refreshToken != null) {
            // 토큰에서 Claims 추출
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 이메일 추출
            String email = claims.getSubject();

            // UserDetailsService를 통해 사용자 정보 가져오기
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            // Authentication 객체 생성
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return null; // 토큰이 유효하지 않은 경우 null 반환
    }
}

