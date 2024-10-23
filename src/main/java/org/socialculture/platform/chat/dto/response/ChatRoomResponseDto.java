package org.socialculture.platform.chat.dto.response;

import lombok.Builder;
import org.socialculture.platform.chat.entity.ChatRoomEntity;

@Builder
public record ChatRoomResponseDto(
        Long chatRoomId,
        Long performanceId,
        Long memberId,
        Long managerId
) {
    // fromEntity 메서드
    public static ChatRoomResponseDto fromEntity(ChatRoomEntity chatRoomEntity) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoomEntity.getChatRoomId())
                .performanceId(chatRoomEntity.getPerformance().getPerformanceId())
                .memberId(chatRoomEntity.getMember().getMemberId())
                .managerId(chatRoomEntity.getManager().getMemberId())
                .build();
    }
}