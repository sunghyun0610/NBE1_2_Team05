package org.socialculture.platform.chat.dto.response;

import lombok.Builder;
import org.socialculture.platform.chat.entity.ChatMessageEntity;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponseDto(
        Long messageId,
        Long chatRoomId,
        Long senderId,
        String senderName,
        String messageContent,
        LocalDateTime sentAt
) {
    // fromEntity 메서드
    public static ChatMessageResponseDto fromEntity(ChatMessageEntity chatMessageEntity) {
        return ChatMessageResponseDto.builder()
                .messageId(chatMessageEntity.getMessageId())
                .chatRoomId(chatMessageEntity.getChatRoomEntity().getChatRoomId())
                .senderId(chatMessageEntity.getSender().getMemberId())
                .senderName(chatMessageEntity.getSender().getName())
                .messageContent(chatMessageEntity.getMessageContent())
                .sentAt(chatMessageEntity.getSentAt())
                .build();
    }
}