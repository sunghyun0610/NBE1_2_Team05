package org.socialculture.platform.chat.repository;

import org.socialculture.platform.chat.entity.ChatMessageEntity;
import org.socialculture.platform.chat.repository.querydsl.ChatMessageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>, ChatMessageRepositoryCustom {
}