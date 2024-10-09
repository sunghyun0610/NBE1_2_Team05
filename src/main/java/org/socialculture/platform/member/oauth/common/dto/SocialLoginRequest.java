package org.socialculture.platform.member.oauth.common.dto;

public record SocialLoginRequest(
        String code,
        String state
) {
}
