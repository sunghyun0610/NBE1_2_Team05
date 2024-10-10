package org.socialculture.platform.member.entity;

public enum MemberRole {
    ROLE_USER("일반 사용자"),
    ROLE_PADMIN("공연 관리자"),
    ROLE_ADMIN("최종 관리자");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
