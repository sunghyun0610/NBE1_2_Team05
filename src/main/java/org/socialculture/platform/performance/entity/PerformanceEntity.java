package org.socialculture.platform.performance.entity;

import jakarta.persistence.*;
import org.socialculture.platform.global.entity.BaseEntity;

@Entity(name = "performance")
public class Performance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "date_start_time")




}
