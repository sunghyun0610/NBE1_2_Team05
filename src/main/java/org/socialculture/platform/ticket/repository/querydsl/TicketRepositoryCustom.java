package org.socialculture.platform.ticket.repository.querydsl;

import org.socialculture.platform.ticket.entity.TicketEntity;

import java.util.List;

/**
 * QueryDSL 을 사용하기 위한 repo
 *
 * @author ycjung
 */
public interface TicketRepositoryCustom {

    List<TicketEntity> getAllTicketsByEmailWithPageAndSortOptionDesc(String email, long offset, int pageSize, String sortOption);
}
