package org.socialculture.platform.chat.repository.querydsl;

import org.socialculture.platform.chat.entity.ChatRoomEntity;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    // QueryDSL을 이용한 특정 회원의 채팅방 목록 조회
    List<ChatRoomEntity> getChatRoomsByMemberEmail(String email);
}
