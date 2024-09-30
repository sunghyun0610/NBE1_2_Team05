package org.socialculture.platform.ticket.repository;

import org.socialculture.platform.ticket.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 repo
 *
 * @author ycjung
 */
public interface DslTicketRepository {

    List<TicketEntity> getAllTicketsByMemberId(long memberId);
    List<TicketEntity> getAllTicketsByMemberIdWithPage(long memberId, long offset, int pageSize);
    List<TicketEntity> getAllTicketsByMemberIdWithPageAndSortOptionDesc(long memberId, long offset, int pageSize, String sortOption);
}
