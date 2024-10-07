package org.socialculture.platform.member.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 사용자가 권한이 없을 때 403 Forbidden 상태 코드를 응답으로 보내는 핸들러
 * @author yeonsu
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 응답 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 코드 설정
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정

        // JSON 형식의 에러 메시지 맵 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Access denied");
        errorResponse.put("message", "권한이 없는 사용자입니다.");

        // 객체를 JSON 문자열로 변환하여 응답 본문에 작성
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

