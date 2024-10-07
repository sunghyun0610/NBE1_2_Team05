package org.socialculture.platform.member.dto.request;


import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.MemberRole;
import org.socialculture.platform.member.entity.SocialProvider;



public record SocialRegisterRequest(
        String email,
        String providerId,
        String name,
        SocialProvider provider
) {

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .email(this.email)
                .name(this.name)
                .provider(this.provider)
                .providerId(this.providerId)
                .role(MemberRole.ROLE_USER)
                .build();
    }


    public static SocialRegisterRequest create(String email, String providerId,
                                               String name, SocialProvider provider) {

        return new SocialRegisterRequest(email, providerId, name, provider);
    }
}
