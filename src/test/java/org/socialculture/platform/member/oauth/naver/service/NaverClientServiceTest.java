package org.socialculture.platform.member.oauth.naver.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.socialculture.platform.member.oauth.naver.dto.NaverUserInfoResponseDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NaverClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NaverClientService naverClientService;

    private String clientId = "test_client_id";
    private String clientSecret = "test_client_secret";
    private String redirectURI = "test_redirect_uri";

    @BeforeEach
    void setUp() {
        naverClientService = new NaverClientService(clientId, redirectURI, clientSecret, restTemplate);
    }

    @Test
    @DisplayName("네이버 사용자 정보 요청 테스트")
    public void getMemberInfoTest() {
        // Given
        String accessToken = "mock_access_token";
        String reqUrl = "https://openapi.naver.com/v1/nid/me";

        // Mock 응답 바디 설정
        String mockResponseBody = "{\"resultcode\":\"00\",\"message\":\"success\",\"response\":{\"email\":\"test@example.com\",\"name\":\"Test User\"}}";

        // Mock ResponseEntity 설정
        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        // RestTemplate.exchange 호출 시 mockResponseEntity 반환하도록 설정
        when(restTemplate.exchange(
                eq(reqUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // When
        NaverUserInfoResponseDTO userInfo = naverClientService.getMemberInfo(accessToken);

        // Then
        assertNotNull(userInfo);
        assertEquals("test@example.com", userInfo.email());
        assertEquals("Test User", userInfo.name());

        // 요청이 올바르게 구성되었는지 검증
        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq(reqUrl), eq(HttpMethod.POST), captor.capture(), eq(String.class));

        HttpEntity<?> capturedRequest = captor.getValue();
        HttpHeaders actualHeaders = capturedRequest.getHeaders();

        // Authorization 헤더 검증
        assertEquals("Bearer " + accessToken, actualHeaders.getFirst("Authorization"));
    }
}