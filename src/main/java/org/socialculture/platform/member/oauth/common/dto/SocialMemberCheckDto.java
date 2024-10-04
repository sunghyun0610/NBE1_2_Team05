package org.socialculture.platform.member.oauth.common.dto;

import lombok.Builder;
import org.socialculture.platform.member.entity.SocialProvider;

/**
 * 소셜 사용자가 가입되어있지 않았을 때, 회원가입을 위해 필요한 정보
 * @param email
 * @param providerId
 * @param provider
 */
@Builder
public record SocialMemberCheckDto(
        String email,
        String providerId,
        SocialProvider provider
) {
    public static SocialMemberCheckDto create(String email, String providerId, SocialProvider provider) {
        return SocialMemberCheckDto.builder()
                .providerId(providerId)
                .provider(provider)
                .email(email)
                .build();
    }
}
