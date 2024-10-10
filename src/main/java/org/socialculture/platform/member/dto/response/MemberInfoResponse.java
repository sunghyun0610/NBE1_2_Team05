package org.socialculture.platform.member.dto.response;

import org.socialculture.platform.member.entity.MemberEntity;

public record MemberInfoResponse(
        String email,
        String name,
        String role
) {
    public static MemberInfoResponse fromEntity(MemberEntity memberEntity) {
        return new MemberInfoResponse(
                memberEntity.getEmail(),
                memberEntity.getName(),
                memberEntity.getRole().getDescription()
        );
    }
}