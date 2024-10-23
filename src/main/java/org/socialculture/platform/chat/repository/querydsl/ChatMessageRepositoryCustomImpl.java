package org.socialculture.platform.chat.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.chat.entity.ChatMessageEntity;
import org.socialculture.platform.chat.entity.QChatMessageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}