package org.socialculture.platform.chat.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.socialculture.platform.chat.entity.ChatRoomEntity;
import org.socialculture.platform.chat.entity.QChatRoomEntity;
import org.socialculture.platform.member.entity.QMemberEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ChatRoomEntity> getChatRoomByPerformanceIdAndMemberId(Long performanceId, Long memberId) {
        QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;

        ChatRoomEntity chatRoom = queryFactory
                .selectFrom(chatRoomEntity)
                .where(
                        chatRoomEntity.performance.performanceId.eq(performanceId),
                        chatRoomEntity.member.memberId.eq(memberId)
                )
                .fetchOne();

        return Optional.ofNullable(chatRoom);
    }

    @Override
    public List<ChatRoomEntity> getChatRoomsByEmail(String email, boolean isManager) {
        QChatRoomEntity chatRoomEntity = QChatRoomEntity.chatRoomEntity;
        QMemberEntity memberEntity = QMemberEntity.memberEntity;

        return queryFactory.selectFrom(chatRoomEntity)
                .join(isManager ? chatRoomEntity.manager : chatRoomEntity.member, memberEntity) // manager 또는 member로 조인
                .where(memberEntity.email.eq(email)) // 이메일 조건으로 필터링
                .fetch();
    }

}
