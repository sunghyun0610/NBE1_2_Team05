package org.socialculture.platform.member.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 401 Unauthorized 상태 코드를 응답으로 보내는 핸들러
 * @author yeonsu
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String message = (String) request.getAttribute("message");

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
        response.setContentType("application/json"); // JSON 형식으로 설정
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정

        // JSON 형식의 에러 메시지 맵 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", message);

        String accessToken = (String) request.getAttribute("accessToken");
        String refreshToken = (String) request.getAttribute("refreshToken");

        if (accessToken != null && refreshToken != null) {
            errorResponse.put("accessToken", accessToken);
            errorResponse.put("refreshToken", refreshToken);
        }

        // 객체를 JSON 문자열로 변환하여 응답 본문에 작성
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

