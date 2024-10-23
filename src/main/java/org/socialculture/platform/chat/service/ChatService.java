package org.socialculture.platform.chat.service;

import org.socialculture.platform.chat.dto.request.ChatMessageRequestDto;
import org.socialculture.platform.chat.dto.request.ChatRoomRequestDto;
import org.socialculture.platform.chat.dto.response.ChatMessageResponseDto;
import org.socialculture.platform.chat.dto.response.ChatRoomResponseDto;

import java.util.List;

public interface ChatService {

    // 새로운 채팅방 생성
    ChatRoomResponseDto createChatRoom(String email, ChatRoomRequestDto chatRoomRequestDto);

    // 채팅 메시지 전송
    ChatMessageResponseDto sendMessage(String email, Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto);

    // 특정 채팅방의 모든 메시지 조회
    List<ChatMessageResponseDto> getMessagesByChatRoom(Long chatRoomId);

    // 특정 유저의 채팅방 목록 조회
    List<ChatRoomResponseDto> getChatRoomsByMember(String email);
}