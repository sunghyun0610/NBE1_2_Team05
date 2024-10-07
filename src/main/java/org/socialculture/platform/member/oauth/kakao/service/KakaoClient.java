package org.socialculture.platform.member.oauth.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.member.entity.SocialProvider;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;
import org.socialculture.platform.member.oauth.common.service.SocialClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoClient implements SocialClient {

    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    @Override
    public String getAccessToken(String code, String state) {
        throw new UnsupportedOperationException("카카오에서는 지원하지 않습니다.");
    }

    public KakaoClient(@Value("${spring.kakao.client_id}") String clientId,
                       @Value("${spring.kakao.redirect_uri}") String redirectUri,
                       @Value("${spring.kakao.client_secret}") String clientSecret
                        ){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }


    /**
     * 클라이언트에서 인가 코드 받아서 access_token 반환
     * @param code
     * @return access_token
     * @throws JsonProcessingException
     */
    @Override
    public String getAccessToken(String code) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        String responseBody = webClient.post()
                .uri("/oauth/token")
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Invalid request")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server error")))
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    /**
     * accessToken을 이용하여 카카오에서 유저의 정보(이메일, id)를 가져온다.
     * @param accessToken
     * @return email, id
     * @throws JsonProcessingException
     */
    @Override
    public SocialMemberCheckDto getMemberInfo(String accessToken) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        String responseBody = webClient.post()
                .uri("/v2/user/me")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new RuntimeException("Invalid request")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new RuntimeException("Server error")))
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String providerId = jsonNode.get("id").asText();

        return SocialMemberCheckDto.create(email, providerId, SocialProvider.KAKAO);
    }


}
