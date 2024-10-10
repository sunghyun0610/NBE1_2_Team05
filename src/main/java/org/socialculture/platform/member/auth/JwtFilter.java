package org.socialculture.platform.member.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String accessToken = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        try {
            if (StringUtils.hasText(accessToken)) {
                jwtTokenProvider.validateToken(accessToken);  // 토큰 검증

                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);  // 인증 객체 생성
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            } else {
                log.debug("JWT 토큰이 없습니다., uri: {}", requestURI);
            }

        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            sendErrorResponse(httpServletResponse, "만료된 액세스 토큰입니다.");
            return;  // 에러 발생 시 더 이상 필터 체인을 호출하지 않음
        } catch (MalformedJwtException | SecurityException e) {
            log.info("잘못된 JWT 서명입니다.");
            sendErrorResponse(httpServletResponse, "잘못된 JWT 서명입니다.");
            return;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            sendErrorResponse(httpServletResponse, "지원되지 않는 JWT 토큰입니다.");
            return;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            sendErrorResponse(httpServletResponse, "JWT 토큰이 잘못되었습니다.");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // JWT 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 401 에러와 함께 JSON 메시지를 반환하는 메서드
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        if (response.isCommitted()) {
            log.debug("응답이 이미 커밋되어서 메시지 추가 불가");
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
