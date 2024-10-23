package org.socialculture.platform.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.socialculture.platform.chat.dto.ChatMessageDto;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // (memberId + chatRoomId) 조합으로 세션을 관리
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long memberId = getMemberIdFromSession(session); // JWT 토큰에서 memberId 추출
        Long chatRoomId = getChatRoomIdFromSession(session); // 요청에서 chatRoomId 추출

        String sessionKey = generateSessionKey(memberId, chatRoomId); // (memberId + chatRoomId) 키 생성
        sessions.put(sessionKey, session);

        System.out.println("WebSocket 연결됨: " + session.getId() + ", 사용자 ID: " + memberId + ", 채팅방 ID: " + chatRoomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("메시지 수신: " + payload);

        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);

        // 채팅방에 있는 다른 사용자의 세션으로 메시지 전송
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String sessionKey = entry.getKey();
            WebSocketSession receiverSession = entry.getValue();

            // 동일한 채팅방에 속한 사용자의 세션에만 메시지를 전송
            if (sessionKey.contains(String.valueOf(chatMessageDto.getChatRoomId()))) {
                if (receiverSession.isOpen()) {
                    receiverSession.sendMessage(new TextMessage(chatMessageDto.getContent()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long memberId = getMemberIdFromSession(session);
        Long chatRoomId = getChatRoomIdFromSession(session);

        String sessionKey = generateSessionKey(memberId, chatRoomId);
        sessions.remove(sessionKey);

        System.out.println("WebSocket 연결 종료됨: " + session.getId() + ", 사용자 ID: " + memberId + ", 채팅방 ID: " + chatRoomId);
    }

    // memberId + chatRoomId 키 생성 메서드
    private String generateSessionKey(Long memberId, Long chatRoomId) {
        return memberId + "_" + chatRoomId;  // 예시로 'memberId_chatRoomId' 형식 사용
    }

    private Long getMemberIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();  // 쿼리 스트링 가져오기
        Map<String, String> params = Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));

        return Long.parseLong(params.get("memberId")); // memberId 추출
    }

    private Long getChatRoomIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();  // 쿼리 스트링 가져오기
        Map<String, String> params = Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));

        return Long.parseLong(params.get("chatRoomId")); // chatRoomId 추출
    }
}