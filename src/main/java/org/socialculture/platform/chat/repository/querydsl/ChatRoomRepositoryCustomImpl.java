package org.socialculture.platform.chat.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.chat.entity.ChatRoomEntity;
import org.socialculture.platform.chat.entity.QChatRoom;
import org.socialculture.platform.member.entity.QMemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomEntity> getChatRoomsByMemberEmail(String email) {
        QChatRoom chatRoomEntity = QChatRoom.chatRoom;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        return queryFactory.selectFrom(chatRoomEntity)
                .join(chatRoomEntity.member, memberEntity) // ChatRoom의 member와 MemberEntity를 조인
                .where(memberEntity.email.eq(email)) // 이메일 조건으로 필터링
                .fetch();
    }
}
