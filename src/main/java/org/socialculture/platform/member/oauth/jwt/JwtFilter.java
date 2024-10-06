package org.socialculture.platform.member.oauth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * HTTP 요청에서 jwt를 추출하고 검증하여 인증 정보를 SecurityContext에 설정하는 필터
 *
 * @author yeonsu
 */
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization"; //jwt가 포함될 HTTP 요청 헤더의 이름
    private final JwtTokenProvider jwtTokenProvider; //jwt 생성 및 검증 처리

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String accessToken = resolveToken(httpServletRequest); // 액세스 토큰, 리프레시 토큰 반환

        String requestURI = httpServletRequest.getRequestURI();

        //1. 액세스 토큰 유효성 검사
        //1.1. 액세스 토큰 유효 -> 정상 처리
        //1.2. 액세스 토큰 만료 -> 만료 응답
        if (StringUtils.hasText(accessToken)) {
            try {
                jwtTokenProvider.validateToken(accessToken);

                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri :{}", authentication.getName(), requestURI);
                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri :{}", authentication.getName(), requestURI);

            } catch (SecurityException | MalformedJwtException e) {
                log.info("잘못된 JWT 서명입니다.");
                httpServletRequest.setAttribute("message", "잘못된 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.info("만료된 JWT 토큰입니다. ");
                httpServletRequest.setAttribute("message", "만료된 액세스 토큰입니다.");
            } catch (UnsupportedJwtException e) {
                log.info("지원되지 않는 JWT 토큰입니다.");
                httpServletRequest.setAttribute("message", "지원되지 않는 JWT 토큰입니다.");
            } catch (IllegalArgumentException e) {
                log.info("JWT 토큰이 잘못되었습니다.");
                httpServletRequest.setAttribute("message", "JWT 토큰이 잘못되었습니다.");
            }
        } else {
            log.debug("JWT 토큰이 없습니다., uri: {}", requestURI);
            httpServletRequest.setAttribute("message", "액세스 토큰이 없습니다.");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    //Request header에서 jwt 토큰 정보를 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7).trim(); // Bearer 다음의 토큰을 반환
        }

        return null; // 토큰이 없을 경우 null 반환
    }
}

