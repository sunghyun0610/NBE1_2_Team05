package org.socialculture.platform.chat.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.chat.entity.ChatMessageEntity;
import org.socialculture.platform.chat.entity.QChatMessageEntity;
import org.socialculture.platform.chat.util.TimeAgoUtil;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatMessageEntity> getMessagesByChatRoomId(Long chatRoomId) {
        QChatMessageEntity chatMessageEntity = QChatMessageEntity.chatMessageEntity;

        return queryFactory.selectFrom(chatMessageEntity)
                .where(chatMessageEntity.chatRoomEntity.chatRoomId.eq(chatRoomId))
                .fetch();
    }

    @Override
    public Map<String, Object> findLastMessageAndTimeAgoForChatRoom(Long chatRoomId) {
        QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;

        // 마지막 메시지 내용과 전송 시간만 조회
        var result = queryFactory
                .select(chatMessage.messageContent, chatMessage.sentAt)
                .from(chatMessage)
                .where(chatMessage.chatRoomEntity.chatRoomId.eq(chatRoomId))
                .orderBy(chatMessage.sentAt.desc())
                .fetchFirst();

        // 조회 결과가 없으면 null 반환
        if (result == null) {
            return null;
        }

        // 결과를 Map에 담기
        Map<String, Object> map = new HashMap<>();
        map.put("lastMessage", result.get(chatMessage.messageContent));

        // 시간 경과를 계산하여 timeAgo 값 추가
        LocalDateTime sentAt = result.get(chatMessage.sentAt);
        map.put("timeAgo", TimeAgoUtil.getElapsedTime(sentAt));

        return map;
    }
}