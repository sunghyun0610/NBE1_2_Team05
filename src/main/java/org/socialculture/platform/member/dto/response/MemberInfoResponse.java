package org.socialculture.platform.member.dto.response;

import org.socialculture.platform.member.entity.MemberEntity;

public record MemberInfoResponse(
        String email,
        String name,
        String role
) {
    public static MemberInfoResponse fromEntity(MemberEntity memberEntity) {
        String roleDescription = switch (memberEntity.getRole()) {
            case ROLE_USER -> "일반 사용자";
            case ROLE_PADMIN -> "공연관리자";
            case ROLE_ADMIN -> "최종관리자";
        };
        return new MemberInfoResponse(
                memberEntity.getEmail(),
                memberEntity.getName(),
                roleDescription
        );
    }
}
