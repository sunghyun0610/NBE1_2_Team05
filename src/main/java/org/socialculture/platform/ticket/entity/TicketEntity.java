package org.socialculture.platform.ticket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.global.entity.BaseEntity;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.performance.entity.PerformanceEntity;

import java.time.LocalDateTime;

/**
 * 티켓 엔티티
 *
 * @author ycjung
 */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
public class TicketEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "performance_id", nullable = false)
    private PerformanceEntity performance;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price;

}


