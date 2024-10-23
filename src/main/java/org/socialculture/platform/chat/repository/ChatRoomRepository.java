package org.socialculture.platform.chat.repository;

import org.socialculture.platform.chat.entity.ChatRoomEntity;
import org.socialculture.platform.chat.repository.querydsl.ChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>, ChatRoomRepositoryCustom {
}
