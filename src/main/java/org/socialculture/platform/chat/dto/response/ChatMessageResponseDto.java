package org.socialculture.platform.chat.dto.response;

import lombok.Builder;
import org.socialculture.platform.chat.entity.ChatMessageEntity;
import org.socialculture.platform.chat.util.TimeAgoUtil;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponseDto(
        Long messageId,
        Long chatRoomId,
        Long senderId,
        String senderName,
        String messageContent,
        LocalDateTime sentAt,
        String formattedSentTime, // 채팅 화면에서 채팅 시간 설정
        int unreadCount
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
                .formattedSentTime(TimeAgoUtil.formatToAmPm(chatMessageEntity.getSentAt())) // 변경된 필드명 반영
                .unreadCount(10)
                .build();
    }
}
