package org.socialculture.platform.chat.dto;

import org.socialculture.platform.chat.util.TimeAgoUtil;

import java.time.LocalDateTime;

// 채팅 목록 업데이트 메시지 DTO
public record ChatListUpdateMessageDto(
        Long chatRoomId,
        String lastMessage,
        int unreadCount,
        String timeAgo // 마지막 채팅 날짜 필드 추가
) {
    // 정적 팩토리 메서드
    public static ChatListUpdateMessageDto of(Long chatRoomId, String lastMessage, int unreadCount, LocalDateTime sentAt) {
        return new ChatListUpdateMessageDto(chatRoomId, lastMessage, unreadCount, TimeAgoUtil.getElapsedTime(sentAt));
    }
}