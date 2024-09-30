package org.socialculture.platform.ticket.repository;

import org.socialculture.platform.ticket.entity.TicketEntity;
import org.socialculture.platform.ticket.repository.querydsl.TicketRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 티켓 레파지토리
 * 
 * @author ycjung
 */
public interface TicketRepository extends JpaRepository<TicketEntity, Long>, TicketRepositoryCustom {
}
