package org.socialculture.platform.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.member.entity.MemberEntity;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;  // 메시지의 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoomEntity;  // 채팅방과 연결 (외래 키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private MemberEntity sender;  // 메시지를 보낸 사람 (외래 키)

    @Column(name = "message_content", nullable = false)
    private String messageContent;  // 메시지 내용

    @Column(name = "sent_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime sentAt;  // 메시지가 전송된 시간 (자동 설정)
}
