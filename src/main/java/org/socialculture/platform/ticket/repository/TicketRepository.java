package org.socialculture.platform.ticket.repository;

import org.socialculture.platform.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 티켓 레파지토리
 * 
 * @author ycjung
 */
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByMemberMemberId(Long memberId);
    Optional<TicketEntity> findByMemberMemberIdAndTicketId(Long memberId, Long ticketId);
}
