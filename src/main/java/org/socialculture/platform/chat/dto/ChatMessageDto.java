package org.socialculture.platform.chat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageDto {
    private Long chatRoomId;      // 채팅방 ID
    private Long senderId;        // 메시지를 보낸 사용자 ID
    private Long receiverId;      // 메시지를 받을 사용자 ID (필요한 경우에만 사용)
    private String content; // 실제 메시지 내용
    private LocalDateTime sentAt;  // 메시지가 전송된 시간
}
