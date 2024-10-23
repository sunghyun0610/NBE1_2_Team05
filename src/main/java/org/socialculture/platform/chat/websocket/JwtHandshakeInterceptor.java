package org.socialculture.platform.chat.websocket;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.member.auth.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 요청 헤더에서 Authorization 헤더 추출
        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // "Bearer " 제거 후 토큰 추출

            try {
                // 토큰 유효성 검사
                jwtTokenProvider.validateToken(token);

                // 토큰에서 이메일 추출
                String email = jwtTokenProvider.getMemberEmailFromToken(token);

                // 이메일로부터 Authentication 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // WebSocket 세션에 인증된 사용자 정보 저장
                attributes.put("user", authentication.getPrincipal());  // 예: 사용자 객체
                attributes.put("email", email);  // 이메일 정보도 저장 가능

                return true;  // 핸드셰이크 성공
            } catch (Exception e) {
                // JWT 검증 실패
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;  // 핸드셰이크 실패
            }
        }

        // Authorization 헤더가 없거나 토큰 형식이 잘못된 경우
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 후의 후처리 로직 (필요시 구현)
    }
}
