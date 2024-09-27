package org.socialculture.platform.performance.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.socialculture.platform.global.entity.BaseEntity;
import org.socialculture.platform.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "performance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "date_start_time")
    private LocalDateTime dateStartTime;

    @Column(name = "date_end_time")
    private LocalDateTime dateEndTime;

    @Column(name = "description")
    private String description;

    @Column(name = "max_audience")
    private int maxAudience;

    @Column(name = "address")
    private String address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "price")
    private int price;

    @Column(name = "remaining_tickets")
    private int remainingTickets;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PerformanceStatus performanceStatus;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceCategoryEntity> performanceCategoryList = new ArrayList<>();
}
