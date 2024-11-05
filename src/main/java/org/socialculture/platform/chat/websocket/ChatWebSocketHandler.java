package org.socialculture.platform.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.socialculture.platform.chat.dto.ChatListUpdateMessageDto;
import org.socialculture.platform.chat.dto.ChatMessageDto;
import org.socialculture.platform.chat.dto.response.ChatMessageResponseDto;
import org.socialculture.platform.chat.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final ChatService chatService;

    private final Set<WebSocketSession> chatListSessions = new HashSet<>(); // 채팅 목록용 WebSocket 세션
    private final Set<WebSocketSession> sessions = new HashSet<>(); // 전체 세션
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());

        // 세션 URI에 따라 채팅방 접속인지, 채팅 목록 접속인지 구분
        if (isChatListSession(session)) {
            chatListSessions.add(session);
        } else {
            Long chatRoomId = getChatRoomIdFromSession(session);
            sessions.add(session);
            chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new HashSet<>()).add(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        // 페이로드를 ChatMessageDto로 변환
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        Long chatRoomId = getChatRoomIdFromSession(session);
        chatMessageDto.setChatRoomId(chatRoomId);

        // 채팅 메시지 저장 및 전송
        ChatMessageResponseDto savedMessage = chatService.saveMessage(chatMessageDto);

        // 현재 채팅방에 연결된 세션에 메시지 전송
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);
        sendMessageToChatRoom(savedMessage, chatRoomSession);

        // 채팅 목록에 실시간 업데이트 전송
        notifyChatListUpdate(chatRoomId, savedMessage);

        // 불필요 세션 정리
        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());

        sessions.remove(session);
        chatListSessions.remove(session); // 채팅 목록 세션도 제거

        // 모든 채팅방에서 해당 세션 제거
        chatRoomSessionMap.values().forEach(sessions -> sessions.remove(session));
    }

    private void notifyChatListUpdate(Long chatRoomId, ChatMessageResponseDto savedMessage) {
        // 채팅 목록에 전송할 업데이트 메시지 생성
        ChatListUpdateMessageDto updateMessage = ChatListUpdateMessageDto.of(
                chatRoomId,
                savedMessage.messageContent(),
                savedMessage.unreadCount(),
                savedMessage.sentAt()
        );

        // 모든 채팅 목록 세션에 전송
        chatListSessions.forEach(session -> sendMessage(session, updateMessage));
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sess.isOpen());
    }

    private void sendMessageToChatRoom(ChatMessageResponseDto chatMessageResponseDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageResponseDto));
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    // 채팅 목록 세션인지 확인하는 메서드
    private boolean isChatListSession(WebSocketSession session) {
        return session.getUri().getPath().contains("/chat-list");
    }

    // 세션에서 chatRoomId 가져오는 유틸리티 메서드
    private Long getChatRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        return query != null && query.contains("chatRoomId=") ?
                Long.parseLong(query.split("chatRoomId=")[1].split("&")[0]) : null;
    }
}

