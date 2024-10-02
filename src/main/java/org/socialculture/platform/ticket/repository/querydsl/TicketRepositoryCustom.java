package org.socialculture.platform.ticket.repository.querydsl;

import org.socialculture.platform.ticket.entity.TicketEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * QueryDSL 을 사용하기 위한 repo
 *
 * @author ycjung
 */
public interface TicketRepositoryCustom {

    List<TicketEntity> getAllTicketsByEmailWithPageAndSortOption(String email, Pageable pageable);
    Optional<TicketEntity> getTicketByEmailAndTicketId(String email, Long ticketId);
}
