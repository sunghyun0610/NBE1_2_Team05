package org.socialculture.platform.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    USER,
    PADMIN,
    ADMIN;

    private String role;

}
