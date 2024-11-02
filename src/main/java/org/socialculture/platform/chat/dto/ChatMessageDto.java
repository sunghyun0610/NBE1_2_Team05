package org.socialculture.platform.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    // 메시지 타입 : 입장, 채팅, 퇴장
//    public enum MessageType {
//        ENTER, TALK, LEAVE
//    }

    // private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 방 번호
    private Long senderId; // 채팅을 보낸 사람
    private String senderEmail;
    private String message; // 메시지

    // chatRoomId, senderEmail, message를 담은 정적 팩토리 메서드
    public static ChatMessageDto of(Long chatRoomId, String senderEmail, String message) {
        return ChatMessageDto.builder()
                .chatRoomId(chatRoomId)
                .senderEmail(senderEmail)
                .message(message)
                .build();
    }

    @Builder
    public ChatMessageDto(Long chatRoomId, Long senderId, String senderEmail, String message) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.message = message;
    }
}
