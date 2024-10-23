package org.socialculture.platform.chat.repository;

import org.socialculture.platform.chat.entity.ChatRoomEntity;
import org.socialculture.platform.chat.repository.querydsl.ChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, ChatRoomRepositoryCustom {
    // 특정 공연, 사용자, 관리자에 해당하는 채팅방이 있는지 확인하는 쿼리 (카멜케이스 적용)
    Optional<ChatRoomEntity> findByPerformanceIdAndMemberIdAndManagerId(Long performanceId, Long memberId, Long managerId);
}
