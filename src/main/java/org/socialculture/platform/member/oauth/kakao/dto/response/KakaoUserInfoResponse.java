package org.socialculture.platform.member.oauth.kakao.dto.response;

public record KakaoUserInfoResponse(
        String email,
        String providerId
) {}

