package org.socialculture.platform.member.entity;

import lombok.Getter;

@Getter
public enum Provider {
    LOCAL,
    KAKAO,
    NAVER;

    private String providerName;
}
