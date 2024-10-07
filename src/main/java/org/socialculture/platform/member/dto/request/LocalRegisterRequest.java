package org.socialculture.platform.member.dto.request;

import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.MemberRole;
import org.socialculture.platform.member.entity.SocialProvider;


public record LocalRegisterRequest(
        String email,
        String password,
        String name
) {


    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .role(MemberRole.ROLE_USER)
                .provider(SocialProvider.LOCAL)
                .build();
    }
}
