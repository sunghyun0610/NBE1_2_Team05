package org.socialculture.platform.chat.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.chat.entity.ChatMessageEntity;
import org.socialculture.platform.chat.entity.QChatMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatMessageEntity> getMessagesByChatRoomId(Long chatRoomId) {
        QChatMessage chatMessageEntity = QChatMessage.chatMessage;

        return queryFactory.selectFrom(chatMessageEntity)
                .where(chatMessageEntity.chatRoom.chatRoomId.eq(chatRoomId))
                .fetch();
    }
}