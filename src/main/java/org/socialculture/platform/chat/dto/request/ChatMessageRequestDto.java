package org.socialculture.platform.chat.dto.request;

import lombok.Builder;

@Builder
public record ChatMessageRequestDto(
        String messageContent
) {
}
