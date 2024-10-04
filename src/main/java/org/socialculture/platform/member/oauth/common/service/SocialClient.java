package org.socialculture.platform.member.oauth.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.socialculture.platform.member.oauth.common.dto.SocialMemberCheckDto;

public interface SocialClient {
    default String getAccessToken(String code) throws JsonProcessingException {
        throw new UnsupportedOperationException("해당 provider에서는 제공 불가");
    }

    String getAccessToken(String code, String state);

    SocialMemberCheckDto getMemberInfo(String accessToken) throws JsonProcessingException;
}

