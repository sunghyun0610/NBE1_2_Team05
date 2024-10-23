package org.socialculture.platform.chat.repository.querydsl;

import org.socialculture.platform.chat.entity.ChatMessageEntity;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    // QueryDSL을 이용한 특정 채팅방의 메시지 목록 조회
    List<ChatMessageEntity> getMessagesByChatRoomId(Long chatRoomId);
}
