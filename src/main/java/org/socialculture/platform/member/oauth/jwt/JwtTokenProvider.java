package org.socialculture.platform.member.oauth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
public class JwtTokenProvider implements InitializingBean {
    private final String secretKey; // JWT 비밀키
    private Key key; // JWT 서명에 사용할 키
    private final long accessTokenValidity; // 액세스 토큰 유효 시간
    private final long refreshTokenValidity; // 리프레시 토큰 유효 시간

    private static final String AUTHORITIES_KEY = "auth"; // 권한 키

    // 생성자
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access.token.expiration}") long accessTokenValidity,
                            @Value("${jwt.refresh.token.expiration}") long refreshTokenValidity) {
        this.secretKey = secretKey;
        this.accessTokenValidity = accessTokenValidity * 1000;
        this.refreshTokenValidity = refreshTokenValidity * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes); // Base64로 디코딩하여 안전한 키 생성
    }

    //액세스 토큰 생성
    public String createAccessToken(Authentication authentication) {
        // 권한 값을 받아 하나의 문자열로 합침
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = new Date(now + this.accessTokenValidity);

        // email을 직접 가져와서 claim에 추가
        String email = authentication.getName(); // 사용자 정보에서 email 가져오기

        return Jwts.builder()
                .setSubject(authentication.getName()) // 페이로드 주제 정보
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보 저장
                .claim("email", email) // email 정보 추가
                .signWith(key, SignatureAlgorithm.HS256) // 서명 설정
                .setExpiration(validity) // 만료 시간 설정
                .compact();
    }

    // 액세스 토큰 생성
//    public String createAccessToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("email", email);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity)) // 밀리초 단위
                .signWith(key, SignatureAlgorithm.HS256) // 새로운 메서드 사용
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰으로 사용자 이메일 가져오기
    public String getMemberEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}

